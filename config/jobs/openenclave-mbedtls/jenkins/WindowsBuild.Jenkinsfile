pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES')
    }

    environment {
        // Shared library config, check out common.groovy!
        SHARED_LIBRARY="/config/jobs/openenclave-mbedtls/jenkins/common.groovy"
    }

    agent {
        label "ACC-${WINDOWS_VERSION}"
    }
    stages {
        stage('Checkout') {
            steps{
                cleanWs()
                checkout scm
            }
        }
        stage('Build and Test') {
            steps{
                script{
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    String[] BUILD_TYPES=['Debug', 'Release']
                    for(BUILD_TYPE in BUILD_TYPES) {
                        stage("${params.WINDOWS_VERSION} Build - ${BUILD_TYPE}") {
                            try{
                                runner.cleanup()
                                runner.checkout("${params.PULL_NUMBER}")
                                runner.cmakeBuildOpenEnclaveMbedTLS("${BUILD_TYPE}","${params.COMPILER}")
                            } catch (Exception e) {
                                // Do something with the exception 
                                error "Program failed, please read logs..."
                            } finally {
                                runner.cleanup()
                            }
                        }
                    } 
                }
            }
        }
    }
    post ('Clean Up') {
        always{
            cleanWs()
        }
    }
}
