// Pull Request Information
PULL_NUMBER=env.PULL_NUMBER?env.PULL_NUMBER:"master"

// OS Version Configuration
LINUX_VERSION=env.LINUX_VERSION?env.LINUX_VERSION:"1804"

// Some Defaults for general build info
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"
COMPILER=env.COMPILER?env.COMPILER:"clang-8"
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
SHARED_LIBRARY="/test-infra/config/jobs/openenclave/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 180, unit: 'MINUTES') 
    }
    agent none
    stages {
        // Go through Build stages
        stage('PR-Check'){
            parallel{
            ///*
                stage('SGX1-FLC'){
                    agent { label 'DOCKER && SGX1 && FLC && !KSS && !OFF' }
                    //when {
                    //    expression { return params.SGX1_FLC == "true" }
                    //}
                    steps{
                        script{
                            echo 'hello'
                            echo '$(PULL_NUMBER)
                            echo $(SGX1_FLC)
                            def PLATFORM_TYPE = "SGX1-FLC"
                            def runner = load pwd() + "${SHARED_LIBRARY}"
                            stage("Clean"){
                                cleanWs()
                                checkout scm
                                //runner.ContainerClean("oetools-full-18.04:${DOCKER_TAG}","--device /dev/sgx --device /dev/mei0 --cap-add=SYS_PTRACE --user=jenkins --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave")
                            }
                            /*     // Build and test in Hardware mode, do not clean up as we will package
                            stage("CheckCI"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    //runner.ContainerCheckCI("oetools-minimal-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--user=root --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave","","${PULL_NUMBER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                }
                            }
                            */     //Build and test in Hardware mode, do not clean up as we will package
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE} - ${BUILD_TYPE}"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    runner.ContainerBuild("oetools-full-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--device /dev/sgx --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave","${EXTRA_CMAKE_ARGS}","${PULL_NUMBER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                }
                            }
                        }
                    }
                }
                //*/
                ///*
                stage('SGX1-FLC-KSS'){
                    agent { label 'DOCKER && SGX1 && FLC && KSS && !OFF' }
                    when {
                        expression { return env.SGX_FLC_KSS ==~ /(?i)(Y|YES|T|TRUE|ON|RUN)/ }
                    }
                    steps{
                        script{
                            def PLATFORM_TYPE = "SGX1-FLC"
                            def runner = load pwd() + "${SHARED_LIBRARY}"
                            stage("Clean"){
                                cleanWs()
                                checkout scm
                                //runner.ContainerClean("oetools-full-18.04:${DOCKER_TAG}","--device /dev/sgx --device /dev/mei0 --cap-add=SYS_PTRACE --user=jenkins --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave")
                            }
                            /*     // Build and test in Hardware mode, do not clean up as we will package
                            stage("CheckCI"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    //runner.ContainerCheckCI("oetools-minimal-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--user=root --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave","","${PULL_NUMBER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                }
                            }
                            */     //Build and test in Hardware mode, do not clean up as we will package
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE} - ${BUILD_TYPE}"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    runner.ContainerBuild("oetools-full-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--device /dev/sgx --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave","${EXTRA_CMAKE_ARGS}","${PULL_NUMBER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                }
                            }
                        }
                    }
                }
                //*/
                ///*
                stage('SGX1'){
                    agent { label 'DOCKER && SGX1 && !FLC && !OFF' }
                    when {
                        expression { return env.SGX_LLC ==~ /(?i)(Y|YES|T|TRUE|ON|RUN)/ }
                    }
                    steps{
                        cleanWs()
                        checkout scm
                        script{
                            def PLATFORM_TYPE = "SGX1"
                            def runner = load pwd() + "${SHARED_LIBRARY}"
                            stage("Clean"){
                                cleanWs()
                                checkout scm
                                //runner.ContainerClean("oetools-full-18.04:${DOCKER_TAG}","--device /dev/sgx --device /dev/mei0 --cap-add=SYS_PTRACE --user=jenkins --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave")
                            }
                            // Build and test in Hardware mode, do not clean up as we will package
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE} - ${BUILD_TYPE}"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    runner.ContainerBuild("oetools-sgx1-llc-full-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--device /dev/isgx --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /var/run/aesmd/aesm.socket:/var/run/aesmd/aesm.socket --volume /jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave:/jenkinsdata/workspace/Pipelines/OpenEnclave-TestInfra/openenclave","${EXTRA_CMAKE_ARGS}","${PULL_NUMBER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                }
                            }
                        }
                    }
                }
                //*/
            }
        }
    }
    post ('Clean Up'){
        always{
            cleanWs()
        }
    }
}
