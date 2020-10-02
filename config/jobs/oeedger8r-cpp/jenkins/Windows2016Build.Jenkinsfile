CTEST_TIMEOUT_SECONDS = 480
REPO_OWNER = env.REPO_OWNER
REPO_NAME = env.REPO_NAME
PULL_NUMBER = env.PULL_NUMBER
TEST_INFRA = env.TEST_INFRA
TEST_INFRA ? PULL_NUMBER = "master" : null

pipeline {
    agent { label 'SGXFLC-Windows-2016-DCAP' }
    stages {
        stage('Win 2016 Build Release') {
            steps {
                script {
                    // Run in Containers once docker is configured on base machines
                    //docker.image('openenclave/windows-2016:0.1').inside('-it --device="class/17eaf82e-e167-4763-b569-5b8273cef6e1"') { c ->
                    checkout()
                    cmake_build_windows("Release")
                    //}
                }
            }
        }
        stage('Win 2016 Build Debug') {
            steps {
                script {
                    //docker.image('openenclave/windows-2016:0.1').inside('-it --device="class/17eaf82e-e167-4763-b569-5b8273cef6e1"') { c ->
                    checkout()
                    cmake_build_windows("Debug")
                    //}
                }
            }
        }
    }
}


void checkout() {
    bat """
        (if exist ${REPO_NAME} rmdir /s/q ${REPO_NAME}) && \
        git clone https://github.com/${REPO_OWNER}/${REPO_NAME} && \
        cd ${REPO_NAME} && \
        git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/* && \
        if NOT %PULL_NUMBER%==master git checkout origin/pr/${PULL_NUMBER}
        """
}

void cmake_build_windows( String buildConfig ) {
    bat """
        cd ${REPO_NAME} && \
        mkdir build && cd build &&\
        vcvars64.bat x64 && \
        cmake.exe .. -G Ninja -DCMAKE_BUILD_TYPE=${buildConfig} && \
        ninja -v -j 4 && \
        ctest.exe -V --output-on-failure --timeout ${CTEST_TIMEOUT_SECONDS}
        """
}
