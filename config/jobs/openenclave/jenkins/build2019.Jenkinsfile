//ECI_LIB_VERSION = env.OECI_LIB_VERSION ?: "master"
//oe = library("OpenEnclaveCommon@${OECI_LIB_VERSION}").jenkins.common.Openenclave.new()

GLOBAL_TIMEOUT_MINUTES = 120
CTEST_TIMEOUT_SECONDS = 1200
GLOBAL_ERROR = null

DOCKER_TAG = env.DOCKER_TAG ?: "latest"
AGENTS_LABELS = [
    "acc-win2019-docker": "SGXFLC-Windows-2019-Docker"
]

def windowsDockerbuild(String label, String tag = 'latest') {
    def node_label = AGENTS_LABELS[label]
    node(node_label) {
        timeout(GLOBAL_TIMEOUT_MINUTES) {
            stage("Checkout") {
                cleanWs()
                checkout scm
            }

            stage("Build SGX Win 2019 Docker Image") {
                echo "build"
                //docker.build(tag, " -f images/windows/2019/Dockerfile . -t windows-2019:latest")
            }

            stage("Test SGX Win 2019 Docker Image"){
                agent {
                    docker { image 'docker.io/openenclave/windows-2019' }
                }

                steps {
                    bat """
                        git clone https://github.com/openenclave/openenclave && \
                        vcvars64.bat x64 && \
                        cmake.exe ${WORKSPACE} -G Ninja -DBUILD_ENCLAVES=ON -DNUGET_PACKAGE_PATH=C:\\Downloads\\prereqs\\nuget -DCPACK_GENERATOR=NuGet -Wdev && \
                        ninja.exe && \
                        ctest.exe -V --timeout ${timeoutSeconds} && \
                        cpack.exe -D CPACK_NUGET_COMPONENT_INSTALL=ON -DCPACK_COMPONENTS_ALL=OEHOSTVERIFY && \
                        cpack.exe && \
                        (if exist C:\\oe rmdir /s/q C:\\oe) && \
                        nuget.exe install open-enclave -Source %cd% -OutputDirectory C:\\oe -ExcludeVersion && \
                        set CMAKE_PREFIX_PATH=C:\\oe\\open-enclave\\openenclave\\lib\\openenclave\\cmake && \
                        cd C:\\oe\\open-enclave\\openenclave\\share\\openenclave\\samples && \
                        setlocal enabledelayedexpansion && \
                        for /d %%i in (*) do (
                            cd C:\\oe\\open-enclave\\openenclave\\share\\openenclave\\samples\\"%%i"
                            mkdir build
                            cd build
                            cmake .. -G Ninja -DNUGET_PACKAGE_PATH=C:\\Downloads\\prereqs\\nuget || exit /b %errorlevel%
                            ninja || exit /b %errorlevel%
                            ninja run || exit /b %errorlevel%
                        )
                        """
                }
            }
        }
    }
}

try{
    windowsDockerbuild('acc-win2019-docker')
} catch(Exception e) {
    println "Caught global pipeline exception: " + e
    GLOBAL_ERROR = e
    throw e
} finally {
    currentBuild.result = (GLOBAL_ERROR != null) ? 'FAILURE' : "SUCCESS"
}
