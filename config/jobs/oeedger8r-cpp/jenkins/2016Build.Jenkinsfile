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

        stage('Win 2016 Build') {
            steps {
                script {
                    docker.image('openenclave/windows-2016:0.1').inside('-it --device="class/17eaf82e-e167-4763-b569-5b8273cef6e1"') { c ->
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
}
