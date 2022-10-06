FROM eclipse-temurin:17

RUN mkdir -p /medispan/downloaded /medispan/installer

COPY build/libs/downloader.jar /medispan/downloader.jar
COPY scripts/downloader.sh /medispan/downloader.sh

ENTRYPOINT ["/medispan/downloader.sh"]

