// Pull Request Information
PULL_NUMBER=env.PULL_NUMBER?env.PULL_NUMBER:"master"

// OS Version Configuration
LINUX_VERSION=env.LINUX_VERSION?env.LINUX_VERSION:"1804"

// Some Defaults for general build info
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"
COMPILER=env.COMPILER?env.COMPILER:"clang-7"
BUILD_TYPE=env.BUILD_TYPE?env.BUILD_TYPE:"RelWithDebInfo"

// Some override for build configuration
LVI_MITIGATION=env.LVI_MITIGATION?env.LVI_MITIGATION:"ControlFlow"
LVI_MITIGATION_SKIP_TESTS=env.LVI_MITIGATION_SKIP_TESTS?env.LVI_MITIGATION_SKIP_TESTS:"OFF"
USE_SNMALLOC=env.USE_SNMALLOC?env.USE_SNMALLOC:"ON"

// Edge casee, snmalloc will not work on old gcc versions and 1604 default is old. Remove after 1604 deprecation.
USE_SNMALLOC=expression { return COMPILER == 'gcc' && LINUX_VERSION =='1604'}?"OFF":USE_SNMALLOC

// Openenclave extra build configs 
EXTRA_CMAKE_ARGS=env.EXTRA_CMAKE_ARGS?env.EXTRA_CMAKE_ARGS:"-DLVI_MITIGATION=${LVI_MITIGATION} -DLVI_MITIGATION_SKIP_TESTS=${LVI_MITIGATION_SKIP_TESTS} -DUSE_SNMALLOC=${USE_SNMALLOC}"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/openenclave/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 180, unit: 'MINUTES') 
    }
    agent none
    stages {
        // Check out test infra repo as need shared libs
        stage('Checkout'){
            steps{
                cleanWs()
                checkout scm
            }
        }
        // Go through Build stages
        stage('PR-Check: SGX1-FLC'){
            agent { label 'DOCKERSGX1&&FLC' }
            steps{
                script{
                    def PLATFORM_TYPE = "SGX1-FLC"
                    // Build and test in Hardware mode, do not clean up as we will package
                    stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE} - ${BUILD_TYPE}"){
                        try{
                            def runner = load pwd() + "${SHARED_LIBRARY}"
                            runner.cleanup()
                            runner.checkout("${PULL_NUMBER}")
                            runner.ContainerBuild("oetools-full-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--device /dev/sgx --device /dev/mei0 --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave","${EXTRA_CMAKE_ARGS}","${PULL_NUMBER}")
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
