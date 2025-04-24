FROM eclipse-temurin:21
LABEL org.opencontainers.image.source=https://github.com/Bluestep-Systems/medispan-automation
LABEL org.opencontainers.image.description="Free tools to automate Medi-Span®'s software and data downloads and their installations."
HEALTHCHECK NONE

RUN mkdir -p /var/medispan/downloaded

COPY build/libs/m6n-downloader.jar scripts/downloader.sh /opt/medispan/

ENTRYPOINT ["/opt/medispan/downloader.sh"]
