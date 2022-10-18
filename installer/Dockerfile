FROM caffeine01/medispan-downloader

RUN apt-get update && apt-get install -y \
    unzip \
    dos2unix \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /var/medispan/data /var/medispan/downloaded /var/medispan/installer /var/medispan/images

ENTRYPOINT [ "/opt/medispan/installer.sh" ]

COPY scripts/ /opt/medispan/

