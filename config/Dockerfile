  
# Copyright (c) Open Enclave SDK contributors.
# Licensed under the MIT License.

FROM ubuntu:18.04

# UPDATE EVERYTHING!!!
RUN apt-get update && apt-get upgrade -y

RUN apt install -y python3-pip curl git vim curl

RUN curl -sL https://aka.ms/InstallAzureCLIDeb | bash

RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl && \
    chmod +x ./kubectl && \
    mv ./kubectl /usr/local/bin/kubectl && \
    kubectl version --client

RUN curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash

RUN apt install curl gnupg && \
    curl https://bazel.build/bazel-release.pub.gpg | apt-key add - && \
    echo "deb [arch=amd64] https://storage.googleapis.com/bazel-apt stable jdk1.8" | tee /etc/apt/sources.list.d/bazel.list && \
    apt update && apt install bazel -y && \
    apt update && apt full-upgrade -y

# Use the official prow tools to keep in line with their efforts
RUN git clone https://github.com/kubernetes/test-infra.git /tools/