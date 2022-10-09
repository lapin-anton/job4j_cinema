#job4j_cinema

###Cайт бронирования билетов в кинотеатр

---
Главная страница, форма с выбором фильма
![img.png](index_page.png)

Форма выбора ряда
![img.png](select_row.png)

Форма выбора места
![img.png](select_cell.png)

Сообщение об успешном бронировании билета
![img.png](book_success.png)

Сообщение о том, что билет был уже забронирован другим пользователем
![img.png](book_fail.png)

Форма для добавления нового фильма
![img.png](add_film_page.png)

Форма для регистрации нового пользователя
![img.png](add_new_user_page.png)

Форма для авторизации
![img.png](authorization.png)
---
Используемый стек
---
- Java 17
- Apache Maven 3.8.5 
- Spring Boot 2.7.3
- PostgreSQL 13
- Liquibase 3.6.2
- Thymeleaf
---
Требуемое окружение
---
- JDK 17
- PostgreSQL 13
- Браузер
---
Подготовка к запуску приложения
---
- Создать БД cinema хост `jdbc:postgresql://localhost:5432/cinema`
- Выполнить скрипты из директории {root}/db/scripts
- Собрать jar с приложением, выполнив команду `mvn install`
- Запустить приложение, выполнив команду: `java -jar job4j_cinema`
- перейти в браузере по ссылке `http://localhost:8080/index`