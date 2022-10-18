FROM eclipse-temurin:17

RUN mkdir -p /var/medispan/downloaded

COPY build/libs/downloader.jar /opt/medispan/downloader.jar
COPY scripts/downloader.sh /opt/medispan/downloader.sh

ENTRYPOINT ["/opt/medispan/downloader.sh"]
