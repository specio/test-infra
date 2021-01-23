pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES')
    }

    parameters {
        string(name: 'LINUX_VERSION', defaultValue: params.LINUX_VERSION ?:'RHEL-8', description: 'Linux version to build')
        string(name: 'COMPILER', defaultValue: params.COMPILER ?:'clang-8', description: 'Compiler version')
        string(name: 'DOCKER_TAG', defaultValue: params.DOCKER_TAG ?:'latest', description: 'Docker image version')
        string(name: 'PULL_NUMBER', defaultValue: params.PULL_NUMBER ?:'master',  description: 'Branch/PR to build')
    }
    environment {
        SHARED_LIBRARY="/config/jobs/oeedger8r-cpp/jenkins/common.groovy"
    }

    agent {
        label "ACC-${LINUX_VERSION}"
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
                    String[] BUILD_TYPES=['Debug', 'RelWithDebInfo', 'Release']
                    for(BUILD_TYPE in BUILD_TYPES){
                        stage("Ubuntu ${params.LINUX_VERSION} Build - ${BUILD_TYPE}"){
                            try{
                                runner.cleanup()
                                runner.checkout("${params.PULL_NUMBER}")
                                runner.cmakeBuildoeedger8r("${BUILD_TYPE}","${params.COMPILER}")
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
    post ('Clean Up'){
        always{
            cleanWs()
        }
    }
}
