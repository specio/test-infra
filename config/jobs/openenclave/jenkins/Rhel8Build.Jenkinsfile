// Timeout configs
GLOBAL_TIMEOUT_MINUTES = 120
CTEST_TIMEOUT_SECONDS = 1200

// Pull Request Information
OE_PULL_NUMBER=env.OE_PULL_NUMBER?env.OE_PULL_NUMBER:"master"

// Some Defaults
BUILD_TYPE=env.BUILD_TYPE?env.BUILD_TYPE:"Release"

// Some override for build configuration
EXTRA_CMAKE_ARGS = env.EXTRA_CMAKE_ARGS?env.EXTRA_CMAKE_ARGS:""

// Repo hardcoded
REPO="openenclave"

pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES') 
    }
    agent { label 'ACC-RHEL-8' }
    stages {
        stage('RHEL 8 Build') {
            steps {
                script {
                    cleanWs()
                    checkout scm
                    def runner = load pwd() + '/config/jobs/openenclave/jenkins/common.groovy'
                    runner.checkout("${REPO}", "${OE_PULL_NUMBER}")
                    runner.cmakeBuildOE("${REPO}","${BUILD_TYPE}")
                }
            }
        }
    }
}
