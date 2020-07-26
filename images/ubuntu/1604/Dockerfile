FROM ubuntu:16.04

# UPDATE EVERYTHING!!!
RUN apt-get update && apt-get upgrade -y

# Install dependencies linux-headers-azure
RUN apt-get install -y git linux-headers-azure

# Hack until decouple architecture
COPY ./ /
RUN images/scripts/setup.sh
