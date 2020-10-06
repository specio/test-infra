#!/usr/bin/env bash

set -ex
##====================================================================================
##
## Default run with no parameters builds with Debug build-type
##
## Run this from the root of the test-infra image.
## Please note that this script does not install any packages needed for build/test.
## Please install all packages necessary for your test before invoking this script.
##
## For CI runs, the Docker image will contain the necessary packages.
##
##====================================================================================
if [[ $1 == "-h" || $1 == "--help" ]]; then
   echo "Script to fire build and test with specified platform/build-type/test mode" 
   echo " Usage: "
   echo " ./testing/openenclave-mbedtls/make-build.sh.sh"
   echo "        -h or --help to Display usage and exit"
   echo ""
   exit 0
fi

if ! [ -f /.dockerenv ]; then
    echo SUDO=sudo
fi

# Delete the build directory if it exists. This allows calling this script iteratively
# for multiple configurations for a platform.
if [[ -d ./build ]]; then
  ${SUDO} rm -rf ./build 
fi

mkdir build && cd build || exit 1

echo $PWD 

CMAKE="cmake .."

MAKE="make"

# Source dependencies
. /opt/openenclave/share/openenclave/openenclaverc

if ! ${CMAKE}; then
    echo ""
    echo "make failed"
    echo ""
    exit 1
fi


if ! ${MAKE}; then
    echo ""
    echo "make check failed"
    echo ""
    exit 1
fi