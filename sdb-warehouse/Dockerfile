FROM eclipse-temurin:17-jre

LABEL maintainer="sdb"
LABEL version="1.0"
LABEL description="SDB Operator Service"

WORKDIR /app

COPY build/libs/sdb-operator.jar /app/sdb-operator.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/sdb-operator.jar"] 