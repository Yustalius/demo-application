FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .

COPY common-utils common-utils

RUN chmod +x ./gradlew

RUN ./gradlew common-utils:build

FROM eclipse-temurin:21-jre AS final

WORKDIR /app

COPY --from=builder /app/common-utils/build/libs/*.jar /app/common-utils.jar

CMD ["echo", "common-utils library built successfully"]