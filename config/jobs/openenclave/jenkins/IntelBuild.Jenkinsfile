// Pull Request Information
PULL_NUMBER=env.PULL_NUMBER?env.PULL_NUMBER:"master"

DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/openenclave/jenkins/common.groovy"

def ContainerRun(String imageName, String compiler, String task, String runArgs="") {
    docker.withRegistry("https://oenc-jenkins.sclab.intel.com:5000") {
        def image = docker.image(imageName)
        image.pull()
        image.inside(runArgs) {
            dir("${WORKSPACE}/build") {
                Run(compiler, task)
            }
        }
    }
}

pipeline {
    options {
        timeout(time: 180, unit: 'MINUTES') 
    }
    agent { label "TEST" }

    stages {
        stage('Checkout'){
            steps{
                echo "SIemka"
                whoami
                cleanWs()
                checkout scm
            }
        }
        // Go through Build stages
        stage('Build'){
            steps{
                script{
                    def task = """
                               whoami
                               pwd
                               ls -la
                               """
                    ContainerRun("oetools-full-18.04:${DOCKER_TAG}", "clang-7", task, "--device /dev/sgx --device /dev/mei0 --cap-add=SYS_PTRACE --user=root --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost")      
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
