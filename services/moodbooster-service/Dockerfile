FROM adoptopenjdk:11-jre-hotspot as builder
ARG JAR_FILE=target/moodbooster-service.jar
COPY ${JAR_FILE}  moodbooster-service.jar
RUN java -Djarmode=layertools -jar moodbooster-service.jar extract

FROM adoptopenjdk:11-jre-hotspot

RUN useradd -ms /bin/bash spring
USER spring
WORKDIR /usr/app/

COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

