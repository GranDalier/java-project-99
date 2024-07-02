
# Менеджер задач

### Hexlet tests and linter status:

[![Actions Status](https://github.com/GrandVandal/java-project-99/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/GrandVandal/java-project-99/actions)
[![Java CI](https://github.com/GrandVandal/java-project-99/actions/workflows/main.yml/badge.svg)](https://github.com/GrandVandal/java-project-99/actions/workflows/main.yml)
[![Maintainability](https://api.codeclimate.com/v1/badges/9f45eb2208103820bd64/maintainability)](https://codeclimate.com/github/GrandVandal/java-project-99/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/9f45eb2208103820bd64/test_coverage)](https://codeclimate.com/github/GrandVandal/java-project-99/test_coverage)

## Описание

[Task Manager](https://task-manager-kk1h.onrender.com) – система управления задачами, подобная http://www.redmine.org/. Она позволяет ставить задачи, назначать исполнителей и менять их статусы. Для работы с системой требуется регистрация и аутентификация.

![Создание задачи](https://github.com/GranDalier/java-project-99/assets/139870237/51a86aec-6d50-4b29-b256-a27b13c4ad11)
![Спиок задач](https://github.com/GranDalier/java-project-99/assets/139870237/3014ce61-d15d-4750-b916-2208cf9e1450)
![Список пользоателей](https://github.com/GranDalier/java-project-99/assets/139870237/f43f3aa3-ebf6-4fad-a5ce-e503a5c67a2a)
![Список статусов задач](https://github.com/GranDalier/java-project-99/assets/139870237/875f5356-fa1d-48f6-9c8e-c9898eb43a6a)
![Список меток](https://github.com/GranDalier/java-project-99/assets/139870237/a96eed24-c8d7-4032-8ba4-0ff424251f26)

## Стэк

- **Фреймворк**: Spring Boot
- **Базы данных**: H2 (локально) / PostgreSQL (в production), используя ORM Hibernate
- **Развертывание в production**: Docker
- **Документация по API-приложения**: Springdoc Openapi, Swagger
- **Трекинг ошибок**: Sentry
- **Аутентификация**: Spring Security
- **Автоматический маппинг**: Mapstruct
- **Тесты**: JUnit 5, Mockwebserver, Datafaker
- **Отчет о тестах**: Jacoco
- **Проверка качества кода**: Codeclimate, Checkstyle

## Сборка и запуск

В директории проекта выполнить команду:
```
make build app
```
Локально приложеие запускается на порту **8080** по адресу `https://localhost:8080`.

Пароль и логин администратора:
```
hexlet@example.com
qwerty
```
