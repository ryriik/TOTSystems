# Тестовое задание TOT Systems

Для написания проекта использовал SpringBoot и PostgreSQL.

## Запуск

Приложение работает на порту 8080.

Перед запуском нужно настроить в файле application.properties параметры подключения к БД (url, username, password).

Также в application.properties необходимо указать путь до папки с xml файлами в xml.path. При запуске приложения оттуда автоматически импортируются все файлы.

Затем в корне проекта выполнить команду:
```
./mvnw spring-boot:run
```

## Примеры запросов

### GET /securities
Вывод всех ценных бумаг.

### GET /securities/{secId}
Вывод ценной бумаги по secid.

### POST /securities
Создание ценной бумаги. В теле запроса передать JSON объект типа Security.

### PUT /securities/{secId}
Обновление ценной бумаги по secid. В теле запроса передать JSON объект типа Security.

### DELETE /securities/{secId}
Удаление ценной бумаги по secid.

### GET /history?secid=""&trade_date=""
Вывод всей истории операций по всем ценным бумагам с опциональными параметрами secid и trade_date для фильтрации.

### GET /history/{id}
Вывод истории по id.

### POST /history
Создание истории операций ценной бумаги. В теле запроса передать JSON объект типа History.

### PUT /history/{id}
Обновление истории операций ценной бумаги по id. В теле запроса передать JSON объект типа History.

### DELETE /history/{id}
Удаление истории операций ценной бумаги по id.

### GET /history/dop?sort_by=""&emitent_title=""&trade_date=""
Вывод всей истории операций по всем ценным бумагам с опциональными параметрами emitent_title и trade_date для фильтрации и sort_by для сортировки.
Выводятся теги secid, regnumber, name, emitent_title, tradedate, numtrades, open, close (для п.4 задания).
