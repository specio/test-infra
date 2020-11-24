// Pull Request Information
OE_PULL_NUMBER=env.OE_PULL_NUMBER?env.OE_PULL_NUMBER:"master"

// OS Version Configuration
LINUX_VERSION=env.LINUX_VERSION?env.LINUX_VERSION:"1804"

// Some Defaults
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"

// Repo hardcoded
REPO="oeedger8r-cpp"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/"+"${REPO}"+"/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 30, unit: 'MINUTES') 
    }
    agent { label "ACC-${LINUX_VERSION}" }
    stages {
        stage( 'Ubuntu 1804 Build - Debug') {
            steps {
                script {
                    cleanWs()
                    checkout scm
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    runner.cleanup("${REPO}")
                    try{
                        runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                        runner.cmakeBuild("${REPO}","Debug")
                    } catch (Exception e) {
                        // Do something with the exception 
                        error "Program failed, please read logs..."
                    } finally {
                        runner.cleanup("${REPO}")
                    }
                }
            }
        }
        stage( 'Ubuntu 1804 Build - Release') {
            steps {
                script {
                    cleanWs()
                    checkout scm
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    runner.cleanup("${REPO}")
                    try{
                        runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                        runner.cmakeBuild("${REPO}","Release")
                    } catch (Exception e) {
                        // Do something with the exception 
                        error "Program failed, please read logs..."
                    } finally {
                        runner.cleanup("${REPO}")
                    }
                }
            }
        }
        stage( 'Ubuntu 1804 Build - RelWithDebInfo') {
            steps {
                script {
                    cleanWs()
                    checkout scm
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    runner.cleanup("${REPO}")
                    try {
                        runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                        runner.cmakeBuild("${REPO}","RelWithDebInfo")
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
