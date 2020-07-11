WORKSPACE=${1:-".."}
build_type=${2:-debug}
optional_args=${4:}

# Set up directory expectations
mkdir build
cd build

# Build
cmake ${WORKSPACE}                                           \
    -G Ninja                                                 \
    -DCMAKE_BUILD_TYPE=${build_type}                         \
    -DLVI_MITIGATION_BINDIR=/usr/local/lvi-mitigation/bin    \
    ${optional_args}                                         \
    -Wdev
ninja -v