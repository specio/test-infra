#!/bin/bash
set -ex

# CMake Image Validation

for repo in oeedger8r-cpp openenclave
do
  # Remove repo if exists
  if [[ -d ./${repo} ]]; then
    rm -rf ./${repo} || sudo rm -rf ./${repo} 
  fi

  git clone --recursive https://github.com/openenclave/${repo}.git && \
  cd ${repo}
  # Iterate through supported compilers

  for compiler in clang-7 clang-8
  do
    for build in Debug Release RelDebInfo
      do
        ../hack/cmake-build.sh -b=${build} --compiler=${compiler}--hardware_mode
      done
  done
  # cd back to root
  cd ../..
  # remove repo to save space
  if [[ -d ./${repo} ]]; then
    rm -rf ./${repo} || sudo rm -rf ./${repo} 
  fi
done
