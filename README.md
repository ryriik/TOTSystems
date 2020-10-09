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

### POST /securities/import
Импорт ценных бумаг из файлов.

### PUT /securities/{secId}
Обновление ценной бумаги по secid. В теле запроса передать JSON объект типа Security.

### DELETE /securities/{secId}
Удаление ценной бумаги по secid.

### GET /history?secid=""&trade_date=""
Вывод всей истории операций по всем ценным бумагам с опциональными параметрами secid и trade_date для фильтрации.

### GET /history/{id}
Вывод истории по id.

### POST /history
Создание истории. В теле запроса передать JSON-объект типа History.

### POST /history/import
Импорт истории из файлов.

### PUT /history/{id}
Обновление истории по id. В теле запроса передать JSON-объект типа History.

### DELETE /history/{id}
Удаление истории по id.

### GET /history/merged?sort_by=""&emitent_title=""&trade_date=""
Вывод всей истории с опциональными параметрами emitent_title и trade_date для фильтрации и sort_by для сортировки.
Выводятся теги secid, regnumber, name, emitent_title, tradedate, numtrades, open, close (для п.4 задания).
