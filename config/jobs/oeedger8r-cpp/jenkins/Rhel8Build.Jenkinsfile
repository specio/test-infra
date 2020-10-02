CTEST_TIMEOUT_SECONDS = 480
REPO_OWNER = env.REPO_OWNER
REPO_NAME = env.REPO_NAME
PULL_NUMBER = env.PULL_NUMBER

pipeline {
    agent { label 'ACC-RHEL-8' }
    stages {
        stage('RHEL 8 Build Release') {
            steps {
                script {
                    checkout()
                    cmake_build_linux("Release")
                }
            }
        }

        stage('RHEL 8 Build Debug') {
            steps {
                script {
                    checkout()
                    cmake_build_linux("Debug")
                }
            }
        }
    }
}

void checkout() {
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

void cmake_build_linux( String buildConfig ) {
    sh  """
        cd ${REPO_NAME} && \
        mkdir build && cd build &&\
        cmake .. -G Ninja -DCMAKE_BUILD_TYPE=${buildConfig} -Wdev
        ninja -v
        ctest --output-on-failure --timeout ${CTEST_TIMEOUT_SECONDS}
        """
}
