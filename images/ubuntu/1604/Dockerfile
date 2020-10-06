FROM ubuntu:16.04

# UPDATE EVERYTHING!!!
RUN apt-get update && apt-get upgrade -y

# Install dependencies
RUN apt-get install -y git curl

# Copy over build tools
COPY ./ /
RUN images/scripts/setup.sh

# Install latest released version of open-enclave, this is sourced by some repositories 
RUN apt-get install -y open-enclave

# Add CI emulator for ARM architecture
RUN curl https://tcpsbuild.blob.core.windows.net/tcsp-build/OE-CI-emulator-armtz-aarch64.tar.gz | tar xvz --no-same-permissions --no-same-owner
