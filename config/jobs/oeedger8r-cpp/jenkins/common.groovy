// Common oeedger8r-cpp jenkins functions
def cmakeBuildoeedger8r(String BUILD_CONFIG, String COMPILER) {
    if (isUnix()) {
        sh  """
            cd oeedger8r-cpp && \
            mkdir build && cd build &&\
            cmake .. -G Ninja -DCMAKE_BUILD_TYPE=${BUILD_CONFIG} -Wdev
            ninja -v
            ctest --output-on-failure --timeout
            """
    } else {
        bat """
            cd oeedger8r-cpp && \
            mkdir build && cd build &&\
            vcvars64.bat x64 && \
            cmake.exe .. -G Ninja -DCMAKE_BUILD_TYPE=${BUILD_CONFIG} && \
            ninja -v -j 4 && \
            ctest.exe -V --output-on-failure
            """
    }
}

void cleanContainers() {
    if (isUnix()) {
        sh  """
            docker system prune -f
            """ 
    } else {
        bat """
            docker system prune -f
            """
    }
}

// Clean up environment, do not fail on error.
def cleanup() {
    if (isUnix()) {
        try {
                sh  """
                    set +e
                    rm -rf oeedger8r-cpp
                    """
            } catch (Exception e) {
                // Do something with the exception 
                error "Program failed, please read logs..."
            } 
        
    }
}

void checkout( String PULL_NUMBER ) {
    if (isUnix()) {
        sh  """
            git config --global core.compression 0 && \
            rm -rf oeedger8r-cpp && \
            git clone --recursive --depth 1 https://github.com/openenclave/oeedger8r-cpp && \
            cd oeedger8r-cpp && \
            git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/*
            if [[ ${PULL_NUMBER} -ne 'master' ]]; then
                git checkout origin/pr/${PULL_NUMBER}
            fi
            """
    }
    else {
        bat """
            git config --global core.compression 0 && \
            (if exist oeedger8r-cpp rmdir /s/q oeedger8r-cpp) && \
            git clone --recursive --depth 1 https://github.com/openenclave/oeedger8r-cpp && \
            cd oeedger8r-cpp && \
            git fetch origin +refs/pull/*/merge:refs/remotes/origin/pr/*
            if NOT ${PULL_NUMBER}==master git checkout origin/pr/${PULL_NUMBER}
            """
    }
}

return this
