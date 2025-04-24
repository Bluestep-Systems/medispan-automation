FROM ghcr.io/bluestep-systems/m6n-downloader:latest
LABEL org.opencontainers.image.source=https://github.com/Bluestep-Systems/medispan-automation
LABEL org.opencontainers.image.description="Free tools to automate Medi-Span®'s software and data downloads and their installations."
HEALTHCHECK NONE

RUN apt-get update && apt-get install -y \
    unzip \
    dos2unix \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /var/medispan/data /var/medispan/downloaded /var/medispan/installer /var/medispan/images

ENTRYPOINT [ "/opt/medispan/installer.sh" ]

COPY scripts/ /opt/medispan/

