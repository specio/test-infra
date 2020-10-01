CTEST_TIMEOUT_SECONDS = 480

pipeline {
    agent { label 'SGXFLC-Windows-2019-Docker' }
    stages {
        stage('Checkout') {
            steps {
                cleanWs()
                checkout scm
            }
        }

        stage('Win 2019 Build') {
            steps {
                script {
                    bat """
                        vcvars64.bat x64 && \
                        cmake.exe ${WORKSPACE} -G Ninja && \
                        ninja -v -j 4 && \
                        ctest.exe -V --output-on-failure --timeout ${CTEST_TIMEOUT_SECONDS}
                        """
                }
            }
        }
    }
}
