// Common openenclave jenkins functions

/** Checkout openenclave, along with merged pull request. If master is instead passed in, don't check out branch
  * as this is being ran as a validation of master or as a reverse integration test on the test-infra repo.
**/

void checkout( String PULL_NUMBER="master" ) {
    if (isUnix()) {
        sh  """
            git config --global core.compression 0 && \
            rm -rf openenclave && \
            ls -la && \
            git clone --recursive --depth 1 https://github.com/openenclave/openenclave && \
            cd openenclave && \
            git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/*
            if [[ ${PULL_NUMBER} -ne 'master' ]]; then
                git checkout origin/pr/${PULL_NUMBER}
            fi
            """
    }
    else {
        bat """
            git config --global core.compression 0 && \
            (if exist openenclave rmdir /s/q openenclave) && \
            git clone --recursive --depth 1 https://github.com/openenclave/openenclave && \
            cd openenclave && \
            git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/*
            if NOT ${PULL_NUMBER}==master git checkout origin/pr/${PULL_NUMBER}
            """
    }
}

/** Build openenclave based on build config, compiler and platform
  * TODO: Add container support
**/
def cmakeBuildopenenclave( String BUILD_CONFIG="Release", String COMPILER="clang-7", String EXTRA_CMAKE_ARGS ="", String OE_LOG_LEVEL ="false", String SPEC_TEST="ALL") {
    if (isUnix()) {

        sh """#!/usr/bin/env bash
            set -o errexit
            set -o pipefail
            echo "======================================================================="
            echo "Running:     $STAGE_NAME"
            echo "-----------------------------------------------------------------------"
            echo "User:        \$(whoami)"
            echo "Agent:       $NODE_NAME - Hostname( \$(hostname) )"
            echo "http_proxy:  $http_proxy"
            echo "https_proxy: $https_proxy"
            echo "no_proxy:    $no_proxy"
            echo "-----------------------------------------------------------------------"
            echo "Configuration:     ${BUILD_CONFIG}"
            echo "Using compiler:    ${COMPILER}"
            echo "Compilator Params: ${EXTRA_CMAKE_ARGS}"
            echo "OE Log level:      ${OE_LOG_LEVEL}"
            echo "CTest test regex:  ${SPEC_TEST}"
            echo "======================================================================="
            sudo apt install cpuid -y
            if [[ \$(cpuid | grep "SGX launch") == *"true"* ]]; then sudo pm2 resurrect && sleep 5 && sudo pm2 status && curl --noproxy "*" -v -k -G "https://localhost:8081/sgx/certification/v2/rootcacrl"; else echo "Legacy Launch Control detected..."; fi
            echo "======================================================================="
            """
        def c_compiler
        def cpp_compiler
        def compiler_version
        switch(COMPILER) {
            case "clang-8":
                c_compiler = "clang"
                cpp_compiler = "clang++"
                compiler_version = "8"
                break
            case "clang-7":
                c_compiler = "clang"
                cpp_compiler = "clang++"
                compiler_version = "7"
                break
            case "gcc":
                c_compiler = "gcc"
                cpp_compiler = "g++"

                break
            default:
                // This is needed for backwards compatibility with the old
                // implementation of the method.
                c_compiler = "clang"
                cpp_compiler = "clang++"
                compiler_version = "8"
        }
        if (compiler_version) {
            c_compiler += "-${compiler_version}"
            cpp_compiler += "-${compiler_version}"
        }
        
        def ctest_regex = ""
        if(SPEC_TEST != "ALL" && SPEC_TEST != "" ){
            ctest_regex = "-R " + SPEC_TEST
        }
     
        withEnv(["CC=${c_compiler}","CXX=${cpp_compiler}"]) {
            sh  """
                mkdir build
                cd ./build
                pwd
                ls -la
                cmake .. -G Ninja -DCMAKE_BUILD_TYPE=${BUILD_CONFIG} ${EXTRA_CMAKE_ARGS} -DLVI_MITIGATION_BINDIR=/usr/local/lvi-mitigation/bin -DCMAKE_INSTALL_PREFIX:PATH='/opt/openenclave' -DCPACK_GENERATOR=DEB -Wdev
                ninja -v
                apt-get install -y strace
                ctest ${ctest_regex} --output-on-failure --timeout 480
                """
        }
    } else {
        bat """
            vcvars64.bat x64 && \
            cmake.exe .. -G Ninja -DCMAKE_BUILD_TYPE=${BUILD_CONFIG} -DBUILD_ENCLAVES=ON -DNUGET_PACKAGE_PATH=C:/oe_prereqs -DCPACK_GENERATOR=NuGet ${EXTRA_CMAKE_ARGS} -Wdev && \
            ninja.exe && \
            ctest.exe -V -C ${BUILD_CONFIG} --output-on-failure
            """
    }
}

def ContainerClean(String imageName, String runArgs) {
    docker.withRegistry("https://oenc-jenkins.sclab.intel.com:5000") {
        def image = docker.image(imageName)
        image.pull()
        image.inside(runArgs) {
            dir("${WORKSPACE}/openenclave"){
                cleanup()
            }
        }
    }
}

def ContainerBuild(String imageName, String buildType, String compiler, String runArgs, String buildArgs, String pullNumber, String oeLogLevel, String specifiedTest) {
    docker.withRegistry("https://oenc-jenkins.sclab.intel.com:5000") {
        def image = docker.image(imageName)
        image.pull()
        image.inside(runArgs) {
            dir("${WORKSPACE}/openenclave"){
                cmakeBuildopenenclave(buildType,compiler,buildArgs,oeLogLevel, specifiedTest)
            }
        }
    }
}
// Common build and package functionality. WIP

def openenclavepackageInstall( String BUILD_CONFIG="Release", String COMPILER="clang-7", String EXTRA_CMAKE_ARGS ="") {
    dir ('openenclave/build') {
        // Duplicate code here, refactor out once package is complete
        if (isUnix()) {

            sh  """
                echo COMPILER IS ${COMPILER}
                """
            def c_compiler
            def cpp_compiler
            def compiler_version
            switch(COMPILER) {
                case "clang-8":
                    c_compiler = "clang"
                    cpp_compiler = "clang++"
                    compiler_version = "8"
                    break
                case "clang-7":
                    c_compiler = "clang"
                    cpp_compiler = "clang++"
                    compiler_version = "7"
                    break
                case "gcc":
                    c_compiler = "gcc"
                    cpp_compiler = "g++"

                    break
                default:
                    // This is needed for backwards compatibility with the old
                    // implementation of the method.
                    c_compiler = "clang"
                    cpp_compiler = "clang++"
                    compiler_version = "8"
            }
            if (compiler_version) {
                c_compiler += "-${compiler_version}"
                cpp_compiler += "-${compiler_version}"
            }
            // To do, switch ninja to cpack sometimes?

            withEnv(["CC=${c_compiler}","CXX=${cpp_compiler}"]) {
                sh  """
                    sudo ninja -v package
                    sudo ninja -v install
                    cp -r /opt/openenclave/share/openenclave/samples ~/
                    cd ~/samples
                    . /opt/openenclave/share/openenclave/openenclaverc
                    for i in *; do
                        if [ -d \${i} ]; then
                            cd \${i}
                            mkdir build
                            cd build
                            cmake ..
                            make
                            make run
                            cd ../..
                        fi
                    done
                    """
            }
        } else {
            //Missing lvi sample test case
            bat """
                vcvars64.bat x64 && \
                cpack.exe -D CPACK_NUGET_COMPONENT_INSTALL=ON -DCPACK_COMPONENTS_ALL=OEHOSTVERIFY && \
                cpack.exe && \
                (if exist C:\\oe rmdir /s/q C:\\oe) && \
                nuget.exe install open-enclave -Source %cd%\\openenclave\\build -OutputDirectory C:\\oe -ExcludeVersion && \
                set CMAKE_PREFIX_PATH=C:\\oe\\open-enclave\\openenclave\\lib\\openenclave\\cmake && \
                cd C:\\oe\\open-enclave\\openenclave\\share\\openenclave\\samples && \
                setlocal enabledelayedexpansion && \
                for /d %%i in (*) do (
                    cd C:\\oe\\open-enclave\\openenclave\\share\\openenclave\\samples\\"%%i"
                    mkdir -m 775 build
                    cd build
                    cmake .. -G Ninja -DNUGET_PACKAGE_PATH=C:\\oe_prereqs -DLVI_MITIGATION=OFF || exit /b %errorlevel%
                    ninja || exit /b %errorlevel%
                    ninja run || exit /b %errorlevel%
                )
                """
        }
    }
}


// Clean up environment, do not fail on error.
def cleanup() {
    if (isUnix()) {
        try {
                sh  """
                    sudo rm -rf openenclave || rm -rf openenclave || echo 'Workspace is clean'
                    sudo rm -rf /opt/openenclave || rm -rf /opt/openenclave || echo 'Workspace is clean'
                    sudo rm -rf ~/samples || rm -rf ~/samples || echo 'Workspace is clean'
                    """
            } catch (Exception e) {
                // Do something with the exception 
                error "Program failed, please read logs..."
            } 
        
    }
}

return this
