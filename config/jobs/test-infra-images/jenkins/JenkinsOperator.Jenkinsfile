// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/test-infra-images/jenkins/common.groovy"

pipeline {
    options {
        timeout(time: 60, unit: 'MINUTES') 
    }
    agent { label "ACC-${LINUX_VERSION}" }

    stages {

        stage('Checkout'){
            steps{
                cleanWs()
                checkout scm
            }
        }

        stage('Docker Build Image'){
            steps{
                script{
                    def builder = load pwd() + "${SHARED_LIBRARY}"
                    def tag = builder.get_image_id()
                    customImage  = builder.dockerImage("openenclave/jenkinsoperator:${tag}", "images/jenkinsoperator/Dockerfile")
                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub_credential_id') {
                        customImage.push()
                    }
                }
            }
        }

        stage('Docker Test Image'){
            steps{
                script{
                    sh 'echo "Tests passed"' 
                }
            }
        }

        stage('Docker Push Image'){
            steps{
                script{
                    sh 'echo "Push Image! "' 
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
