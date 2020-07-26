FROM registry.access.redhat.com/ubi8/ubi:8.1

# UPDATE EVERYTHING!!!
RUN yum update -y && yum upgrade -y

# Install dependencies
RUN yum install -y git

# Copy over build tools
COPY ./ /
