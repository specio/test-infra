CTEST_TIMEOUT_SECONDS = 480
PULL_NUMBER = env.PULL_NUMBER
TEST_INFRA = env.TEST_INFRA
TEST_INFRA ? PULL_NUMBER = "master" : null

pipeline {
    agent { label 'ACC-RHEL-8' }
    stages {
        stage('RHEL 8 Build Release') {
            steps {
                script {
                    checkout("openenclave","oeedger8r-cpp")
                    cmake_build_linux("oeedger8r-cpp","Release")
                }
            }
        }
        stage('RHEL 8 Build RelWithDebInfo') {
            steps {
                script {
                    checkout("openenclave","oeedger8r-cpp")
                    cmake_build_linux("oeedger8r-cpp","RelWithDebInfo")
                }
            }
        }
        stage('RHEL 8 Build Debug') {
            steps {
                script {
                    checkout("openenclave","oeedger8r-cpp")
                    cmake_build_linux("oeedger8r-cpp","Debug")
                }
            }
        }
    }
}


void checkout(String REPO_OWNER, String REPO_NAME ) {
    sh  """
        rm -rf ${REPO_NAME} && \
        git clone https://github.com/${REPO_OWNER}/${REPO_NAME} && \
        cd ${REPO_NAME} && \
        git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/*
        if [[ $PULL_NUMBER -ne 'master' ]]; then
            git checkout origin/pr/${PULL_NUMBER}
        fi
        """
}

void cmake_build_linux( String REPO_NAME, String BUILD_CONFIG ) {
    sh  """
        cd ${REPO_NAME} && \
        mkdir build && cd build &&\
        cmake .. -G Ninja -DCMAKE_BUILD_TYPE=${BUILD_CONFIG} -Wdev
        ninja -v
        ctest --output-on-failure --timeout ${REPO_NAME}
        """
}
