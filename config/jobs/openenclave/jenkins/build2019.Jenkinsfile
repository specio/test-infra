// Copyright (c) Open Enclave SDK contributors.
// Licensed under the MIT License.

OECI_LIB_VERSION = env.OECI_LIB_VERSION ?: "master"
oe = library("OpenEnclaveCommon@${OECI_LIB_VERSION}").jenkins.common.Openenclave.new()

GLOBAL_TIMEOUT_MINUTES = 120
CTEST_TIMEOUT_SECONDS = 1200
GLOBAL_ERROR = null

DOCKER_TAG = env.DOCKER_TAG ?: "latest"
AGENTS_LABELS = [
    "acc-win2019-docker": "SGXFLC-Windows-2019-Docker"
]

def windowsDockerbuild(String label, String tag = 'latest') {
    def node_label = AGENTS_LABELS[label]
    stage("Windows ${label} ${build_type} with SGX ${has_quote_provider} LVI_MITIGATION=${lvi_mitigation}") {
        node(node_label) {
            timeout(GLOBAL_TIMEOUT_MINUTES) {
                stage("Checkout") {
                    cleanWs()
                    checkout scm
                }

                stage("Build SGX Win 2019 Docker Image") {
                    docker.build(tag, " -f images/windows/2019/Dockerfile .")
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
