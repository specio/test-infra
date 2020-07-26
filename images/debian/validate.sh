if [[ -d ./openenclave ]]; then
  rm -rf ./openenclave || sudo rm -rf ./openenclave 
fi

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