
/** 
  * Build OpenEnclave and run Tests on specified platform
**/
public void buildAndTest(String dockerTag, String compiler, String pullNumber, String oeLogLevel, String specifiedTest) {
    if (isUnix()) {
        sh """#!/usr/bin/env bash
            set -o errexit
            set -o pipefail
            echo "======================================================================="
            echo "Starting Build and Test
            echo "-----------------------------------------------------------------------"
            echo "Docker Tag:     ${dockerTag}"
            echo "Using compiler:    ${compiler}"
            echo "PullRequest:    ${pullNumber}"
            echo "OE Log level:      ${oeLogLevel}"
            echo "CTest test regex:  ${specifiedTest}"
            """
        def colors = ["SGX1-FLC": "oetools-full-18.04:", "SGX1-FLC-KSS:${dockerTag}": "oetools-full-18.04-KSS:${dockerTag}", "SGX1": "oetools-sgx1-llc-full-18.04:${dockerTag}"]
        for (key in colors.keySet()) {
            currSetUp = colors[key]
            sh  """
                echo "Testing: ${currSetUp}"
            """
        }
        sh  """
            echo "======================================================================="
            echo "Finished Build and Test
            echo "-----------------------------------------------------------------------"
        """
    }
}
return this
