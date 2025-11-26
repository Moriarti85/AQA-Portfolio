# Используем официальный образ Maven с Java 17
FROM 3.9.11-amazoncorretto-11 AS builder

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