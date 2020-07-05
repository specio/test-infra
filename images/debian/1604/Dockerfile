FROM ubuntu:16.04

# UPDATE EVERYTHING!!!
RUN apt-get update && apt-get upgrade -y

# Install dependencies linux-headers-5.3.0-1028-azure
RUN apt-get install -y git linux-headers-5.3.0-1028-azure

# Hack until decouple architecture
COPY ./ /
RUN images/debian/setup.sh