WORKSPACE=${1:-".."}
BUILD_TYPE=${2:-Debug}
OPTIONAL_ARGS=${4:-""}
CTEST_TIMEOUT_SECONDS=${5:-400}

# Set up directory expectations
mkdir build
cd build

# Build
cmake ${WORKSPACE}                                           \
    -G Ninja                                                 \
    -DCMAKE_BUILD_TYPE=${build_type}                         \
    -DLVI_MITIGATION_BINDIR=/usr/local/lvi-mitigation/bin    \
    ${OPTIONAL_ARGS}                                         \
    -Wdev
ninja -v

# Test
ctest --output-on-failure --timeout ${CTEST_TIMEOUT_SECONDS}