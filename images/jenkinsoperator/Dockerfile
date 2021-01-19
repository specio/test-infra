FROM debian:buster

WORKDIR /workspace
RUN mkdir -p /workspace
ENV WORKSPACE=/workspace \
    TERM=xterm

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    ca-certificates \
    curl \
    file \
    git \
    jq \
    mercurial \
    openssh-client \
    pkg-config \
    procps \
    python \
    python3 \
    python-dev \
    python-openssl \
    python-pip \
    python3-pip \
    rsync \
    unzip \
    wget \
    xz-utils \
    zip \
    zlib1g-dev \
    && rm -rf /var/lib/apt/lists/* \
    && python -m pip install --upgrade pip setuptools wheel

# Install python modules for jenkins controller
RUN pip install requests docopt
RUN pip3 install --upgrade pip3
RUN pip3 install requests docopt urllib3

# Copy over build tools
COPY ./ /
