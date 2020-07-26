FROM centos:7

# UPDATE EVERYTHING!!!
RUN yum update -y && yum upgrade -y

# Install dependencies
RUN yum install -y git

# Hack until decouple architecture
COPY ./ /
