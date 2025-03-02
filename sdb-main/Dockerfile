FROM gradle:jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM openjdk:21-jdk-slim
COPY --from=build /home/gradle/src/build/libs/sdb-main.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]