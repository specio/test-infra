// Pull Request Information
PULL_NUMBER=env.PULL_NUMBER?env.PULL_NUMBER:"master"

// OS Version Configuration
LINUX_VERSION=env.LINUX_VERSION?env.LINUX_VERSION:"1804"

// Some Defaults
DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"
COMPILER=env.COMPILER?env.COMPILER:"clang-7"
BUILD_TYPE=env.BUILD_TYPE?env.BUILD_TYPE:"RelWithDebInfo"

// Hardware and simulation build modes. 1 is simulation, 0 is hardware
String[] SIMULATION_MODES=[0,1]

// Some override for build configuration
LVI_MITIGATION=env.LVI_MITIGATION?env.LVI_MITIGATION:"ControlFlow"
LVI_MITIGATION_SKIP_TESTS=env.LVI_MITIGATION_SKIP_TESTS?env.LVI_MITIGATION_SKIP_TESTS:"OFF"
USE_SNMALLOC=env.USE_SNMALLOC?env.USE_SNMALLOC:"ON"
// Remove once 1604 is deprecated, 1604 gcc snmalloc will not work. Handle edge case explictly
USE_SNMALLOC=expression { COMPILER == 'gcc' }?"OFF":USE_SNMALLOC

// TODO Implement simulatioN mode just default for now
OE_SIMULATION=env.OE_SIMULATION?1:0
// Do not package on simulation
PACKAGE=expression { OE_SIMULATION == '0' }?"ON":"OFF"

EXTRA_CMAKE_ARGS=env.EXTRA_CMAKE_ARGS?env.EXTRA_CMAKE_ARGS:"-DLVI_MITIGATION=${LVI_MITIGATION} -DLVI_MITIGATION_SKIP_TESTS=${LVI_MITIGATION_SKIP_TESTS} -DUSE_SNMALLOC=${USE_SNMALLOC}"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/openenclave/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 180, unit: 'MINUTES') 
    }
    agent { label "ACC-${LINUX_VERSION}" }

    stages {
        // Check out test infra repo as need shared libs
        stage('Checkout'){
            steps{
                cleanWs()
                checkout scm
            }
        }
        // Go through Build stages
        stage('Build'){
            steps{
                script{
                    def runner = load pwd() + "${SHARED_LIBRARY}"

                    // Build and test in Hardware mode, do not clean up as we will package
                    stage("Ubuntu ${LINUX_VERSION} Build - ${BUILD_TYPE}"){
                        try{
                            runner.cleanup()
                            runner.checkout("${PULL_NUMBER}")
                            runner.cmakeBuildopenenclave("${BUILD_TYPE}","${COMPILER}","${EXTRA_CMAKE_ARGS}")
                        } catch (Exception e) {
                            // Do something with the exception 
                            error "Program failed, please read logs..."
                        }
                    }

                    // Build package and test installation work flows, clean up after
                    stage("Ubuntu ${LINUX_VERSION} Package - ${BUILD_TYPE}"){
                        try{
                            runner.openenclavepackageInstall("${BUILD_TYPE}","${COMPILER}","${EXTRA_CMAKE_ARGS}")
                        } catch (Exception e) {
                            // Do something with the exception 
                            error "Program failed, please read logs..."
                        } finally {
                            runner.cleanup()
                        }
                    }

                    // Build in simulation mode 
                    stage("Ubuntu ${LINUX_VERSION} Build - ${BUILD_TYPE} Simulation"){
                        withEnv(["OE_SIMULATION=1"]) {
                            try{
                                runner.cleanup()
                                runner.checkout("${PULL_NUMBER}")
                                runner.cmakeBuildopenenclave("${BUILD_TYPE}","${COMPILER}","${EXTRA_CMAKE_ARGS}")
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
