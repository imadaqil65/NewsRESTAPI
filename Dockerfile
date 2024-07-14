FROM gradle:8.5.0-jdk17

WORKDIR /app

COPY ./build/libs/Individual_News-0.0.1-SNAPSHOT.jar ./

EXPOSE 8090

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar Individual_News-0.0.1-SNAPSHOT.jar"]