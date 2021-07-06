def unixCheckout( String PULL_NUMBER="master" ) {
    sh """#!/usr/bin/env bash
        set -o errexit
        set -o pipefail
        git config --global core.compression 0 && \
        rm -rf openenclave && \
        git clone --recursive --depth 1 https://github.com/openenclave/openenclave && \
        cd openenclave && \
        git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/*
        if [ '${PULL_NUMBER}' != 'master' ]
        then
            echo 'checking out  ${PULL_NUMBER}'
            git checkout origin/pr/${PULL_NUMBER}
        fi
        echo 'Changes checked out...'
        echo "-----------------------------------------------------------------------"
        git log -1
        echo "-----------------------------------------------------------------------"
        """
}

//returns CC and CXX variables as a table
def determineCompilers(String compiler) {
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
    return [c_compiler, cpp_compiler]
}

def cmakeBuildopenenclave( String BUILD_CONFIG="Release", String COMPILER="clang-8", String OE_LOG_LEVEL ="false", String SPEC_TEST="ALL") {
    if (isUnix()) {

        (c_compiler, cpp_compiler) = determineCompilers(compiler)
        def ctest_regex = ""
        if(SPEC_TEST != "ALL" && SPEC_TEST != "" ){
            ctest_regex = "-R " + SPEC_TEST
        }

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
            echo "CC:                ${c_compiler}"
            echo "CXX:               ${cpp_compiler}"
            echo "OE Log level:      ${OE_LOG_LEVEL}"
            echo "CTest test regex:  ${SPEC_TEST}"
            echo "======================================================================="
            sudo apt install cpuid -y
            if [[ \$(cpuid | grep "SGX launch") == *"true"* ]]; then sudo pm2 resurrect && sleep 5 && sudo pm2 status && curl --noproxy "*" -v -k -G "https://localhost:8081/sgx/certification/v2/rootcacrl"; else echo "Legacy Launch Control detected..."; fi
            echo "======================================================================="
            """

        withEnv(["CC=${c_compiler}","CXX=${cpp_compiler}"]) {
            sh  """
                echo "start build..."
                ls -la
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
    }
}

def unixContainerBuild(String imageName, String runArgs, String buildType, String compiler, String oeLogLevel, String specifiedTest) {
    docker.withRegistry("https://oenc-jenkins.sclab.intel.com:5000") {
        def image = docker.image(imageName)
        image.pull()
        image.inside(runArgs) {
            dir("${WORKSPACE}/openenclave"){
                cmakeBuildopenenclave(buildType,compiler,oeLogLevel, specifiedTest)
            }
        }
    }
}

///////////
//////////
/////////
////////

def ContainerClean(String imageName, String runArgs) {
    docker.withRegistry("https://oenc-jenkins.sclab.intel.com:5000") {
        def image = docker.image(imageName)
        image.pull()
        image.inside(runArgs) {
            dir("${WORKSPACE}"){
                cleanup()
            }
        }
    }
}

def ContainerBuild(String imageName, String buildType, String compiler, String runArgs, String pullNumber, String oeLogLevel, String specifiedTest) {
    docker.withRegistry("https://oenc-jenkins.sclab.intel.com:5000") {
        def image = docker.image(imageName)
        image.pull()
        image.inside(runArgs) {
            dir("${WORKSPACE}/openenclave"){
                cmakeBuildopenenclave(buildType,compiler,oeLogLevel, specifiedTest)
            }
        }
    }
}

// Clean up environment, do not fail on error.
def cleanup() {
    if (isUnix()) {
        try {
                sh  """
                    hostname
                    whoami
                    pwd
                    ls -la
                    sudo rm -rf openenclave || rm -rf openenclave || echo 'Workspace is clean'
                    ls -la
                    """
            } catch (Exception e) {
                // Do something with the exception 
                error "Program failed, please read logs..."
            } 
        
    }
}

return this
