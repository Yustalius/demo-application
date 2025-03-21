FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Копируем gradle-файлы
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .

# Копируем модули
COPY common-utils common-utils
COPY sdb-main sdb-main

# Устанавливаем права на исполнение для gradlew
RUN chmod +x ./gradlew

# Собираем проект
RUN ./gradlew sdb-main:bootJar

# Итоговый образ
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем JAR-файл из предыдущего этапа
COPY --from=builder /app/sdb-main/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]