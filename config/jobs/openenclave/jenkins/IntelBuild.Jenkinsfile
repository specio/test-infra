// Pull Request Information
PULL_NUMBER=env.PULL_NUMBER?env.PULL_NUMBER:"master"

// OS Version Configuration
LINUX_VERSION=env.LINUX_VERSION?env.LINUX_VERSION:"Ubuntu-1804"

// Some Defaults for general build info
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"
COMPILER=env.COMPILER?env.COMPILER:"clang-7"
BUILD_TYPE=env.BUILD_TYPE?env.BUILD_TYPE:"RelWithDebInfo"

// Some override for build configuration
LVI_MITIGATION=env.LVI_MITIGATION?env.LVI_MITIGATION:"ControlFlow"
LVI_MITIGATION_SKIP_TESTS=env.LVI_MITIGATION_SKIP_TESTS?env.LVI_MITIGATION_SKIP_TESTS:"OFF"
LVI_MITIGATION_BINDIR=env.DLVI_MITIGATION_BINDIR?env.DLVI_MITIGATION_BINDIR:"/usr/local/lvi-mitigation/bin"
USE_SNMALLOC=env.USE_SNMALLOC?env.USE_SNMALLOC:"ON"
// Hack to disable environment lvi
env.LVI_MITIGATION=""

// Edge casee, snmalloc will not work on old gcc versions and 1604 default is old. Remove after 1604 deprecation.
USE_SNMALLOC=expression { return COMPILER == 'gcc' && LINUX_VERSION =='1604'}?"OFF":USE_SNMALLOC

// Openenclave extra build configs 
EXTRA_CMAKE_ARGS=env.EXTRA_CMAKE_ARGS?env.EXTRA_CMAKE_ARGS:"-DLVI_MITIGATION=${LVI_MITIGATION} -DLVI_MITIGATION_SKIP_TESTS=${LVI_MITIGATION_SKIP_TESTS} -DUSE_SNMALLOC=${USE_SNMALLOC}"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/openenclave/jenkins/common.groovy"

// whether to run as an e2e test
E2E=env.E2E?env.E2E:"OFF"

pipeline {
    options {
        timeout(time: 180, unit: 'MINUTES') 
    }
    agent { label "TEST" }

    stages {
        stage('Checkout'){
            steps{
                cleanWs()
                checkout scm
            }
        }
        // Go through Build stages
        stage('Build'){
            steps{
                script{
                    def runner = load pwd() + "${SHARED_LIBRARY}"

                    // Build and test in Hardware mode, do not clean up as we will package
                    stage("Ubuntu ${LINUX_VERSION} Build - ${BUILD_TYPE}"){
                        try{
                            runner.cleanup()
                            runner.checkout("${PULL_NUMBER}")
                            ls -la
                        } catch (Exception e) {
                            // Do something with the exception 
                            error "Program failed, please read logs..."
                        }
                    }
                }
            }
        }
    }
    post ('Clean Up'){
        always{
            cleanWs()
        }
    }
}
