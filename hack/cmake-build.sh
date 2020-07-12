# Defaults
WORKSPACE=".."
DCMAKE_BUILD_TYPE="Debug"
CTEST_TIMEOUT_SECONDS=400

# Set up directory expectations
mkdir build
cd build

# Build
cmake ${WORKSPACE}                                           \
    -G Ninja                                                 \
    -DCMAKE_BUILD_TYPE=${DCMAKE_BUILD_TYPE}
ninja -v

# Test
ctest --output-on-failure --timeout ${CTEST_TIMEOUT_SECONDS}