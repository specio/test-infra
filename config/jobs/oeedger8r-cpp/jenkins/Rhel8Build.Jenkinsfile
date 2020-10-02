CTEST_TIMEOUT_SECONDS = 480
REPO_OWNER = env.REPO_OWNER
REPO_NAME = env.REPO_NAME
PULL_NUMBER = env.PULL_NUMBER

pipeline {
    agent { label 'ACC-RHEL-8' }
    stages {
        stage('RHEL 8 Build') {
            steps {
                script {
                    checkout()
                    cmake_build_linux()
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
        git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/* && \
        git checkout origin/pr/${PULL_NUMBER}
        """
}

void cmake_build_linux() {
    sh  """
        cd ${REPO_NAME} && \
        mkdir build && cd build &&\
        cmake .. \
        -G Ninja \
        -Wdev
        ninja -v
        ctest --output-on-failure --timeout ${CTEST_TIMEOUT_SECONDS}
        """
}
