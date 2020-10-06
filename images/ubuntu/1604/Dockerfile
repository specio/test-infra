FROM ubuntu:16.04

# UPDATE EVERYTHING!!!
RUN apt-get update && apt-get upgrade -y

# Install dependencies
RUN apt-get install -y git

# Copy over build tools
COPY ./ /
RUN images/scripts/setup.sh

# Install latest released version of open-enclave, this is sourced by some repositories 
RUN apt-get install -y open-enclave