// Pull Request Information
OE_PULL_NUMBER=env.OE_PULL_NUMBER?env.OE_PULL_NUMBER:"master"

// OS Version Configuration
LINUX_VERSION=env.LINUX_VERSION?env.LINUX_VERSION:"1604"

// Some Defaults
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"
COMPILER=env.COMPILER?env.COMPILER:"clang-7"
String[] BUILD_TYPES = ['Debug', 'RelWithDebInfo', 'Release']

// Repo hardcoded
REPO="oeedger8r-cpp"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/"+"${REPO}"+"/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES') 
    }
    agent { label "ACC-${LINUX_VERSION}" }

    stages {
        stage('Build'){
            steps{
                script{
                    for(BUILD_TYPE in BUILD_TYPES){
                        stage("Ubuntu ${LINUX_VERSION} Build - ${BUILD_TYPE}"){
                            script {
                                cleanWs()
                                checkout scm
                                def runner = load pwd() + "${SHARED_LIBRARY}"
                                runner.cleanup("${REPO}")
                                try{
                                    runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                                    runner.cmakeBuildoeedger8r("${REPO}","${BUILD_TYPE}","${COMPILER}")
                                } catch (Exception e) {
                                    // Do something with the exception 
                                    error "Program failed, please read logs..."
                                } finally {
                                    runner.cleanup("${REPO}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
