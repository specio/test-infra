#!/bin/bash
set -ex

# CMake Validation
for repo in oeedger8r-cpp openenclave
do
  # Remove repo if exists
  if [[ -d ./${repo} ]]; then
    rm -rf ./${repo} || sudo rm -rf ./${repo} 
  fi
  for build in Debug Release RelDebInfo
  do
    git clone --recursive https://github.com/openenclave/${repo}.git && \
    cd ${repo} && \
    ../hack/cmake-build.sh --build_package -b=${build} --compiler=clang-7 --hardware_mode && \
    cd ..
  done
  # cd back to root
  cd ..
  # remove repo to save space
  if [[ -d ./${repo} ]]; then
    rm -rf ./${repo} || sudo rm -rf ./${repo} 
  fi
done
