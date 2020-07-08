  
# Copyright (c) Open Enclave SDK contributors.
# Licensed under the MIT License.

FROM ubuntu:18.04

# UPDATE EVERYTHING!!!
RUN apt-get update && apt-get upgrade -y

# Install dependencies
RUN apt install -y python-pip python3-pip curl git vim curl gnupg snapd software-properties-common docker

# Get yq
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys CC86BB64
RUN add-apt-repository ppa:rmescandon/yq
RUN apt update
RUN apt install yq -y

# Get Azure CLI
RUN curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Get Kubectl
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl && \
    chmod +x ./kubectl && \
    mv ./kubectl /usr/local/bin/kubectl

# Get Helm
RUN curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash

# Get Bazel 3.0
RUN curl https://bazel.build/bazel-release.pub.gpg | apt-key add - && \
    echo "deb [arch=amd64] https://storage.googleapis.com/bazel-apt stable jdk1.8" | tee /etc/apt/sources.list.d/bazel.list && \
    apt update && apt install bazel-3.0.0 -y