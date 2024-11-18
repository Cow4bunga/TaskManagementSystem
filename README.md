# Task management system
Тестовое задание в Effective Mobile.
## Get started
Здесь описана последовательность действий для запуска приложения в Docker-контейнере, а также базовые операции, предоставляемые сервисом.
### Запуск
1. Выполните в терминале git clone https://github.com/Cow4bunga/TaskManagementSystem.git , предварительно указав предпочтительную для клонирования директорию.
2. Перейдите в корневую директорию проекта (`cd ~/TaskManagementSystem`).
3. Выполните команду `docker-compose up`.

Приложение запустится на localhost:8083 (**ВАЖНО!** Перед запуском необходимо убедиться, что порты 5432 и 8083 не используются).


### Документация
Документация API для проекта будет доступна по следующему URL: 

`http://localhost:8083/api/v1/api-docs`

Swagger UI доступен по URL:

`http://localhost:8083/api/v1/swagger-ui/index.html`
