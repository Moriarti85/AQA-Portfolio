# 🚀 API Testing Automation Project

![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.9.6-blue)
![JUnit5](https://img.shields.io/badge/JUnit-5.10-brightgreen)
![REST-Assured](https://img.shields.io/badge/REST--Assured-5.4.0-lightgrey)
![Tests](https://img.shields.io/badge/tests-8%20passed-brightgreen)
## 📋 О проекте

Данный проект демонстрирует навыки автоматизации тестирования:

✅ **REST API** - тестирование CRUD операций, валидация JSON схем

✅ **UI тесты** - тестирование веб-интерфейса с Selenide

✅ **Интеграция с CI/CD** - Docker
## 🛠 Технологии
**Java 21** - основной язык программирования

**JUnit 5** - фреймворк для тестирования

**REST Assured** - библиотека для REST API тестирования

**Jackson** - работа с JSON

**Maven** - управление зависимостями

**AssertJ** - fluent assertions

**REST API** - тестирование CRUD операций, валидация JSON схем

**UI тесты** - тестирование веб-интерфейса с Selenide

**Интеграция с CI/CD** - Docker

## 🚀 Быстрый старт
### Предварительные требования
Java 21

Maven 3.6

Git
## 💻 Локальный запуск тестов
### Клонирование репозитория
```bash
   git clone https://github.com/Moriarti85/AQA-Portfolio.git
   cd api-testing-project
```
### Запуск тестов
```bash
   mvn clean test
````
### Запуск с генерацией отчета
```bash
   mvn clean test surefire-report:report
```
## 🐳 Запуск тестов в Docker
### Предварительные требования
- Установленный [Docker](https://docs.docker.com/get-docker/)
- Установленный [Docker Compose](https://docs.docker.com/compose/install/)
- 4+ GB свободной оперативной памяти

### Быстрый запуск
```bash
# Клонировать репозиторий
   git clone https://github.com/Moriarti85/AQA-Portfolio.git
   cd AQA-Portfolio

# Запустить все тесты (API + UI)
   docker-compose up --build
```
Project Link: https://github.com/Moriarti85/AQA-Portfolio
## Скриншоты
### Прогон API тестов
![img.png](ApiTestsRunResult.png)
### Прогон UI тестов
![img.png](UITestsRunResult.png)
### Струкура проекта
![img.png](ProjectStructure.png)