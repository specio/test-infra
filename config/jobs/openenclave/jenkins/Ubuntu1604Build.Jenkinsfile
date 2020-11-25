// Timeout configs
GLOBAL_TIMEOUT_MINUTES = 120
CTEST_TIMEOUT_SECONDS = 1200

// Pull Request Information
OE_PULL_NUMBER=env.OE_PULL_NUMBER?env.OE_PULL_NUMBER:"master"

// OS Version Configuration
LINUX_VERSION=env.LINUX_VERSION?env.LINUX_VERSION:"1604"

// Some Defaults
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"

// Some override for build configuration
EXTRA_CMAKE_ARGS=env.EXTRA_CMAKE_ARGS?env.EXTRA_CMAKE_ARGS:"-DLVI_MITIGATION=ControlFlow -DLVI_MITIGATION_SKIP_TESTS=OFF -DUSE_SNMALLOC=ON"

// Repo hardcoded
REPO="openenclave"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/"+"${REPO}"+"/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 30, unit: 'MINUTES') 
    }
    agent { label "ACC-${LINUX_VERSION}" }
    stages {
        stage( 'Ubuntu 1604 Build - Debug') {
            steps {
                script {
                    cleanWs()
                    checkout scm
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    runner.cleanup("${REPO}")
                    try{
                        runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                        runner.cmakeBuildPackageInstallOE("${REPO}","Debug", "${EXTRA_CMAKE_ARGS}")
                    } catch (Exception e) {
                        // Do something with the exception 
                        error "Program failed, please read logs..."
                    } finally {
                        runner.cleanup("${REPO}")
                    }
                }
            }
        }
        stage( 'Ubuntu 1604 Build - Release') {
            steps {
                script {
                    cleanWs()
                    checkout scm
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    runner.cleanup("${REPO}")
                    try{
                        runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                        runner.cmakeBuildPackageInstallOE("${REPO}","Release", "${EXTRA_CMAKE_ARGS}")
                    } catch (Exception e) {
                        // Do something with the exception 
                        error "Program failed, please read logs..."
                    } finally {
                        runner.cleanup("${REPO}")
                    }
                }
            }
        }
        stage( 'Ubuntu 1604 Build - RelWithDebInfo') {
            steps {
                script {
                    cleanWs()
                    checkout scm
                    def runner = load pwd() + "${SHARED_LIBRARY}"
                    runner.cleanup("${REPO}")
                    try {
                        runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                        runner.cmakeBuildPackageInstallOE("${REPO}","RelWithDebInfo", "${EXTRA_CMAKE_ARGS}")
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