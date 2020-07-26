#!/usr/bin/env bash

set -ex
##====================================================================================
##
## Docker Image Wrapper
##
##====================================================================================
if [[ $1 == "-h" || $1 == "--help" ]]; then
   echo "Script wrapper to build docker images"
   echo " Usage: "
   echo " ./images/test/build.sh"
   echo "        -h or --help to Display usage and exit"
   echo "        --docker_path path to docker file"
   echo "        --docker_tag custom tag to use"
   echo ""
   exit 0
fi

# Build Docker Image. Why are you here if you are overwriting this?!
BUILD=1
# Test  Docker Image. Default is enabled
TEST=1
# Publish Docker Image. Default is deisabled.
PUSH=0
# Tag
TAG="image:latest"
DOCKER_PATH=""

# Parse the command line - keep looping as long as there is at least one more argument
while [[ $# -gt 0 ]]; do
    key=$1
    case $key in
        # This is a flag type option. Will catch --docker_path 
        --docker_path)
        shift # past the key and to the value
        DOCKER_PATH=$1
        ;;
        *)
        echo "Unknown option '${key}'"
        exit 1
        ;;
    esac
    # Shift after checking all the cases to get the next option
    shift
done

if [[ ${BUILD} -eq 1 ]]; then
    docker build . -f ${DOCKER_PATH} -t ${TAG}  || sudo docker build . -f ${DOCKER_PATH} -t ${TAG} 
fi

if [[ ${TEST} -eq 1 ]]; then
   docker run -it ${TAG} bash -c "./images/test/validate.sh" || sudo docker run -it ${TAG} bash -c "./images/test/validate.sh"
fi

if [[ ${PUSH} -eq 1 ]]; then
    # TODO https://kubernetes.io/docs/tasks/inject-data-application/distribute-credentials-secure/
    docker login --username=$DOCKER_USER --password=$DOCKER_PAS
    docker push ${TAG} || sudo docker push ${TAG} 
fi
