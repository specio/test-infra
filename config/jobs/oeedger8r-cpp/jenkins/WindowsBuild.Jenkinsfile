// Pull Request Information
PULL_NUMBER=env.PULL_NUMBER?env.PULL_NUMBER:"master"

// OS Version Configuration
WINDOWS_VERSION=env.WINDOWS_VERSION?env.WINDOWS_VERSION:"Windows-2019"

// Some Defaults
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"
COMPILER=env.COMPILER?env.COMPILER:"MSVC"
String[] BUILD_TYPES=['Debug', 'Release']

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/oeedger8r-cpp/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES') 
    }
    agent { label "ACC-${WINDOWS_VERSION}" }

    stages {
        stage('Checkout'){
            steps{
                cleanWs()
                checkout scm
            }
        }
        stage('Build'){
            steps{
                script{
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    for(BUILD_TYPE in BUILD_TYPES){
                        stage("Windows ${WINDOWS_VERSION} Build - ${BUILD_TYPE}"){
                            script {
                                try{
                                    runner.cleanup()
                                    runner.checkout("${PULL_NUMBER}")
                                    runner.cmakeBuildoeedger8r("${BUILD_TYPE}","${COMPILER}")
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
    }
    post ('Clean Up'){
        always{
            cleanWs()
        }
    }
}
pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES')
    }

    parameters {
        //string(name: 'WINDOWS_VERSION', defaultValue: 'Ubuntu-1804', description: 'Windows version to build')
        string(name: 'COMPILER', defaultValue: params.COMPILER ?:'MSVC')
        string(name: 'DOCKER_TAG', defaultValue: 'latest', description: 'Docker image version')
        string(name: 'PULL_NUMBER', defaultValue: 'master', description: 'Branch/PR to build')
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
