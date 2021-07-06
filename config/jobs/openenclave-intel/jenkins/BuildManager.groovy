
/** 
  * Build OpenEnclave and run Tests on specified platform
**/
public void buildAndTest(String setup, String dockerTag, String compiler, String pullNumber, String oeLogLevel, String specifiedTest) {
    if (isUnix()) {
        sh """#!/usr/bin/env bash
            set -o errexit
            set -o pipefail
            echo "=============================================================================================================================================="
            echo "Starting Build and Test"
            echo "----------------------------------------------------------------------------------------------------------------------------------------------"
            echo "Setup:             ${setup}"
            echo "Docker Tag:        ${dockerTag}"
            echo "Using compiler:    ${compiler}"
            echo "PullRequest:       ${pullNumber}"
            echo "OE Log level:      ${oeLogLevel}"
            echo "CTest test regex:  ${specifiedTest}"
            """

        def Images = 
            [
            "SGX1-FLC"     : "oetools-full-18.04:${dockerTag}", 
            "SGX1-FLC-KSS" : "oetools-full-18.04:${dockerTag}", 
            "SGX1"         : "oetools-sgx1-llc-full-18.04:${dockerTag}"
            ]

        currImage = Images[setup]
        sh """#!/usr/bin/env bash
            set -o errexit
            set -o pipefail
            echo "Testing ${setup} on image: ${currImage}"
        """
        def runner = load pwd() + "/test-infra/config/jobs/openenclave-intel/jenkins/common.groovy"
        runner.checkout("${pullNumber}")

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
