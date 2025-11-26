# Используем официальный образ Maven с Java 17
FROM maven:3.8.6-openjdk-17 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код
COPY src ./src

# Компилируем проект
RUN mvn clean compile test-compile

# Устанавливаем команду по умолчанию для запуска тестов
CMD ["mvn", "test"]