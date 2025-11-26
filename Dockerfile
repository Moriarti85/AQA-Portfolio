# Используем официальный образ Maven с Java 17
FROM maven:3.9.11-amazoncorretto-21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код
COPY src ./src

# Компилируем проект
RUN mvn clean compile test-compile

# Устанавливаем команду для запуска тестов
CMD ["mvn", "test", "-Dselenide.remote=http://selenium-hub:4444/wd/hub", "-Dselenide.browser=chrome"]