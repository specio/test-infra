  
# Copyright (c) Open Enclave SDK contributors.
# Licensed under the MIT License.

FROM ubuntu:18.04

# UPDATE EVERYTHING!!!
RUN apt-get update && apt-get upgrade -y

# Install dependencies
RUN apt install -y python-pip python3-pip curl git vim curl gnupg snapd software-properties-common docker

# Get Bazel 3.0
RUN curl https://bazel.build/bazel-release.pub.gpg | apt-key add - && \
    echo "deb [arch=amd64] https://storage.googleapis.com/bazel-apt stable jdk1.8" | tee /etc/apt/sources.list.d/bazel.list && \
    apt update && apt install bazel-3.0.0 -y

COPY ./ /

RUN /hack/validate-prow-jobs.sh