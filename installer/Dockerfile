FROM medispan-downloader

RUN apt-get update && apt-get install -y \
    unzip \
    dos2unix \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /medispan/data /medispan/drivers /medispan/downloaded /medispan/installer

ENTRYPOINT [ "/medispan/installer.sh" ]

COPY scripts/ /medispan/

