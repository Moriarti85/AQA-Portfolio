# Используем официальный образ Maven с Java 21
FROM maven:3.9.11-amazoncorretto-21

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Компилируем проект
RUN mvn clean compile test-compile -B

# Устанавливаем команду для запуска тестов
CMD ["mvn", "test", "-B"]