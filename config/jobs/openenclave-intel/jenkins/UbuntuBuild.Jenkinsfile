pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES')
    }

    parameters {
        string(name: 'LINUX_VERSION', defaultValue: params.LINUX_VERSION ?:'Ubuntu-1804', description: 'Linux version to build')
        string(name: 'COMPILER', defaultValue: params.COMPILER ?:'clang-8', description: 'Compiler version')
        string(name: 'DOCKER_TAG', defaultValue: params.DOCKER_TAG ?:'latest', description: 'Docker image version')
        string(name: 'PULL_NUMBER', defaultValue: params.PULL_NUMBER ?:'master',  description: 'Branch/PR to build')
        string(name: 'OE_LOG_LEVEL', defaultValue: params.OE_LOG_LEVEL ?:'master',  description: 'LogLevel for OE tests')
        string(name: 'SPEC_TEST', defaultValue: params.SPEC_TEST ?:'master',  description: 'Run specific test(s) - By regex')
    }

    environment {
        SHARED_LIBRARY="/test-infra/config/jobs/openenclave-intel/jenkins/common.groovy"
    }

    agent none
    stages {
        // Go through Build stages
        stage('PR-Check'){
            parallel{
            ///*
                stage('SGX1-FLC'){
                    agent { label 'DOCKER && SGX1 && FLC && !KSS && !OFF' }
                    when {
                        expression { return env.SGX1_FLC != "false" }
                    }
                    steps{
                        script{
                            def PLATFORM_TYPE = "SGX1-FLC"
                            def BUILD_TYPE = "RelWithDebInfo"
                            def runner = load pwd() + "${SHARED_LIBRARY}"
                            stage("Clean"){
                                cleanWs()
                                checkout scm
                                runner.ContainerClean("oetools-full-18.04:${DOCKER_TAG}","--cap-add=SYS_PTRACE --user=jenkins --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave")
                            }
                            /*     // Build and test in Hardware mode, do not clean up as we will package
                            stage("CheckCI"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    //runner.ContainerCheckCI("oetools-minimal-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--user=root --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave","","${PULL_NUMBER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                }
                            }
                            */     //Build and test in Hardware mode, do not clean up as we will package
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE} - ${BUILD_TYPE}"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    runner.ContainerBuild("oetools-full-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--device /dev/sgx --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave","${EXTRA_CMAKE_ARGS}","${PULL_NUMBER}","${OE_LOG_LEVEL}","${SPEC_TEST}")
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
                        expression { return env.SGX1_FLC_KSS != "false" }
                    }
                    steps{
                        script{
                            def PLATFORM_TYPE = "SGX1-FLC-KSS"
                            def runner = load pwd() + "${SHARED_LIBRARY}"
                            stage("Clean"){
                                cleanWs()
                                checkout scm
                                //runner.ContainerClean("oetools-full-18.04:${DOCKER_TAG}","--device /dev/sgx --device /dev/mei0 --cap-add=SYS_PTRACE --user=jenkins --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave")
                            }
                            /*     // Build and test in Hardware mode, do not clean up as we will package
                            stage("CheckCI"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    //runner.ContainerCheckCI("oetools-minimal-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--user=root --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave","","${PULL_NUMBER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                }
                            }
                            */     //Build and test in Hardware mode, do not clean up as we will package
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE} - ${BUILD_TYPE}"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    runner.ContainerBuild("oetools-full-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--device /dev/sgx --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave","${EXTRA_CMAKE_ARGS}","${PULL_NUMBER}","${OE_LOG_LEVEL}","${SPEC_TEST}")
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
                        expression { return env.SGX1_LLC != "false" }
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
                                //runner.ContainerClean("oetools-full-18.04:${DOCKER_TAG}","--device /dev/sgx --device /dev/mei0 --cap-add=SYS_PTRACE --user=jenkins --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave")
                            }
                            // Build and test in Hardware mode, do not clean up as we will package
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE} - ${BUILD_TYPE}"){
                                try{
                                    runner.checkout("${PULL_NUMBER}")
                                    runner.ContainerBuild("oetools-sgx1-llc-full-18.04:${DOCKER_TAG}","${BUILD_TYPE}","${COMPILER}","--device /dev/isgx --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost --volume /var/run/aesmd/aesm.socket:/var/run/aesmd/aesm.socket --volume /jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave:/jenkinsdata/workspace/Pipelines/Intel-IntegrationTests/openenclave","${EXTRA_CMAKE_ARGS}","${PULL_NUMBER}","${OE_LOG_LEVEL}","${SPEC_TEST}")
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
}
