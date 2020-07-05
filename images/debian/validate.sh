# OpenEnclave Validation
git clone --recursive https://github.com/openenclave/openenclave.git && \

# Build set up
cd openenclave && \
mkdir build && \
cd build && \
cmake -G "Ninja" .. && \
ninja && \

# Test
ctest