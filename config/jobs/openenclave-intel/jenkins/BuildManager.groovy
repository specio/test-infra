
/** 
  * Build OpenEnclave and run Tests on specified platform
**/
public void buildAndTest(String setup, String dockerTag, String compiler, String pullNumber, String oeLogLevel, String specifiedTest) {
    if (isUnix()) {
        def Images = 
            [
            "SGX1-FLC"     : "oetools-full-18.04:${dockerTag}", 
            "SGX1-FLC-KSS" : "oetools-full-18.04:${dockerTag}", 
            "SGX1"         : "oetools-sgx1-llc-full-18.04:${dockerTag}"
            ]
        def CustomDockerArgs = 
            [
            "SGX1-FLC"     : "--device /dev/sgx", 
            "SGX1-FLC-KSS" : "--device /dev/sgx", 
            "SGX1"         : "--device /dev/isgx"
            ]
        proxyArgs  = " --env https_proxy=http://proxy-mu.intel.com:912 --env http_proxy=http://proxy-mu.intel.com:911 --env no_proxy=intel.com,.intel.com,localhost"
        volumeArgs = " --volume "+pwd()+"/openenclave:"+pwd()+"/openenclave"
        commonArgs = " --cap-add=SYS_PTRACE --user=root"
        dockerArgs = CustomDockerArgs[setup]+commonArgs+proxyArgs+volumeArgs
        currImage  = Images[setup]
        buildConf  = "RelWithDebInfo"

        sh """#!/usr/bin/env bash
            set -o errexit
            set -o pipefail
            echo "=============================================================================================================================================="
            echo "Starting Build and Test"
            echo "----------------------------------------------------------------------------------------------------------------------------------------------"
            echo "Setup:             ${setup}"
            echo "Docker Image:      ${currImage}"
            echo "PullRequest:       ${pullNumber}"
            echo "Build config:      ${buildConf}"
            echo "Using compiler:    ${compiler}"
            echo "OE Log level:      ${oeLogLevel}"
            echo "CTest test regex:  ${specifiedTest}"
            echo "----------------------------------------------------------------------------------------------------------------------------------------------"
            """

        def runner = load pwd() + "/test-infra/config/jobs/openenclave-intel/jenkins/common.groovy"

        runner.unixCheckout("${pullNumber}")
        runner.unixContainerBuild(currImage, dockerArgs, buildConf, compiler, oeLogLevel, specifiedTest)

        sh """#!/usr/bin/env bash
            set -o errexit
            set -o pipefail
            echo "----------------------------------------------------------------------------------------------------------------------------------------------"
            echo "Finished Build and Test"
            echo "=============================================================================================================================================="
        """
    }
}
return this
