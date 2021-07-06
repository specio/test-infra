pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES')
    }

    parameters {
        string(name: 'LINUX_VERSION', defaultValue: params.LINUX_VERSION ?:'Ubuntu-1804', description: 'Linux version to build')
        string(name: 'COMPILER', defaultValue: params.COMPILER ?:'clang-8', description: 'Compiler version')
        string(name: 'DOCKER_TAG', defaultValue: params.DOCKER_TAG ?:'latest', description: 'Docker image version')
        string(name: 'PULL_NUMBER', defaultValue: params.PULL_NUMBER ?:'master',  description: 'Branch/PR to build')
        string(name: 'OE_LOG_LEVEL', defaultValue: params.OE_LOG_LEVEL ?:'ERROR',  description: 'LogLevel for OE tests')
        string(name: 'SPEC_TEST', defaultValue: params.SPEC_TEST ?:'ALL',  description: 'Run specific test(s) - By regex')
    }

    environment {
        SHARED_LIBRARY="/test-infra/config/jobs/openenclave-intel/jenkins/common.groovy"
    }

    agent none
    stages {
        // Go through Build stages
        stage('PR-Check'){
            parallel{
                stage('SGX1-FLC'){
                    agent { label 'DOCKER && SGX1 && FLC && !KSS && !OFF' }
                    when {
                        expression { return env.SGX1_FLC != "false" }
                    }
                    steps{
                        script{
                            def PLATFORM_TYPE = "SGX1-FLC"
                            def buildManager = load pwd() + "/test-infra/config/jobs/openenclave-intel/jenkins/BuildManager.groovy"
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE}"){
                                try{
                                    buildManager.buildAndTest("${PLATFORM_TYPE}", "${DOCKER_TAG}", "${COMPILER}", "${PULL_NUMBER}", "${OE_LOG_LEVEL}", "${SPEC_TEST}")
                                } catch (Exception e) {
                                    error "Build and Test failed, please read logs..."
                                }
                            }
                        }
                    }
                }
                stage('SGX1-FLC-KSS'){
                    agent { label 'DOCKER && SGX1 && FLC && KSS && !OFF' }
                    when {
                        expression { return env.SGX1_FLC_KSS != "false" }
                    }
                    steps{
                        script{
                            def PLATFORM_TYPE = "SGX1-FLC-KSS"
                            def buildManager = load pwd() + "/test-infra/config/jobs/openenclave-intel/jenkins/BuildManager.groovy"
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE}"){
                                try{
                                    buildManager.buildAndTest("${PLATFORM_TYPE}", "${DOCKER_TAG}", "${COMPILER}", "${PULL_NUMBER}", "${OE_LOG_LEVEL}", "${SPEC_TEST}")
                                } catch (Exception e) {
                                    error "Build and Test failed, please read logs..."
                                }
                            }
                        }
                    }
                }
                stage('SGX1'){
                    agent { label 'DOCKER && SGX1 && !FLC && !OFF' }
                    when {
                        expression { return env.SGX1_LLC != "false" }
                    }
                    steps{
                        script{
                            def PLATFORM_TYPE = "SGX1"
                            def buildManager = load pwd() + "/test-infra/config/jobs/openenclave-intel/jenkins/BuildManager.groovy"
                            stage("Ubuntu ${LINUX_VERSION} - ${PLATFORM_TYPE}"){
                                try{
                                    buildManager.buildAndTest("${PLATFORM_TYPE}", "${DOCKER_TAG}", "${COMPILER}", "${PULL_NUMBER}", "${OE_LOG_LEVEL}", "${SPEC_TEST}")
                                } catch (Exception e) {
                                    error "Build and Test failed, please read logs..."
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
