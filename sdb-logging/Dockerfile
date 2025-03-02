FROM gradle:jdk21 AS build

WORKDIR /home/gradle/src
COPY --chown=gradle:gradle build.gradle ./

RUN gradle dependencies --no-daemon --parallel

COPY --chown=gradle:gradle src ./src

RUN gradle bootJar --no-daemon --parallel

FROM openjdk:21-jdk-slim

COPY --from=build /home/gradle/src/build/libs/sdb-logger.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]