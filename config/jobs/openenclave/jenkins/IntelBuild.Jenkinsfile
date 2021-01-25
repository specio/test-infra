// Pull Request Information
PULL_NUMBER=env.PULL_NUMBER?env.PULL_NUMBER:"master"

DOCKER_TAG=env.DOCKER_TAG?env.DOCKER_TAG:"latest"

// Shared library config, check out common.groovy!
SHARED_LIBRARY="/config/jobs/openenclave/jenkins/common.groovy"

def runTask(String task) {
    dir("${WORKSPACE}/build") {
        sh """#!/usr/bin/env bash
                set -o errexit
                set -o pipefail
                source /etc/profile
                echo "======================================================================="
                echo "Running:     $STAGE_NAME"
                echo "-----------------------------------------------------------------------"
                echo "User:        \$(whoami)"
                echo "Agent:       $NODE_NAME - Hostname( \$(hostname) )"
                echo "http_proxy:  $http_proxy"
                echo "https_proxy: $https_proxy"
                echo "no_proxy:    $no_proxy"
                echo "======================================================================="
                ${task}
            """
    }
}

def Run(String compiler, String task, String compiler_version = "") {
    def c_compiler
    def cpp_compiler
    switch(compiler) {
        case "cross":
            // In this case, the compiler is set by the CMake toolchain file. As
            // such, it is not necessary to specify anything in the environment.
            runTask(task)
            return
        case "clang":
            c_compiler = "clang"
            cpp_compiler = "clang++"
            break
        case "gcc":
            c_compiler = "gcc"
            cpp_compiler = "g++"
            break
        default:
            // This is needed for backwards compatibility with the old
            // implementation of the method.
            c_compiler = "clang"
            cpp_compiler = "clang++"
            compiler_version = "7"
    }
    if (compiler_version) {
        c_compiler += "-${compiler_version}"
        cpp_compiler += "-${compiler_version}"
    }
    withEnv(["CC=${c_compiler}","CXX=${cpp_compiler}"]) {
        runTask(task);
    }
}

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
                sh '''
                    #!/bin/bash
                    echo "hello world"
                    whoami
                    pwd
                    ls -la
                '''
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
