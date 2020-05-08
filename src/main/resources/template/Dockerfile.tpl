FROM adoptopenjdk/openjdk11:alpine-jre

VOLUME /tmp
ENV VERTX_OPTS -Dvertx.cacheDirBase=/tmp/

COPY target/[=artifactId]-deployment /
EXPOSE 8888

HEALTHCHECK --interval=5m \
            --timeout=5s \
            CMD curl -f http://localhost:8888/v1/internal/healthcheck || exit 1

ENTRYPOINT ["sh","-c"]
CMD ["java -Duser.timezone=UTC -Djava.security.egd=file:/dev/./urandom -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory -Dglue.config=$GLUE_CONFIG -jar lib/[=artifactId]-[=version].jar"]
