# Итоговый проект по Java Enterprise Online Project
## ТЗ проекта:
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.
The task is:
Build a voting system for deciding where to have lunch.
- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
* If user votes again the same day: 
  * If it is before 11:00 we asume that he changed his mind.
  * If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

##Инициализая БД
* initDB.sql - инициализая модели БД
* populateDB.sql - тестовые данные

##REST-API:

###Работа с пользователями

#####Получить перечень всех пользователей.:
***
Доступно только администратору
***
GET - http://localhost:8080/restaurant/api/users
***
Входящие параметры:
* (не обязательный параметр) jsonSort - сортировка
***
Результат:
- массив объектов пользователей, зарегистрированных в системе

#####Создать нового пользователя: 
***
Доступно по функционалу создания новой учетной записи с клиентской части (не зарегистрированный пользователь или администратор):
***
POST - http://localhost:8080/restaurant/api/users
***
Входящие параметры:
- объект пользователя, которого необходимо создать
***
Результат:
- объект пользователя, который был создан системой
***
Статус ответа:
- 201

#####Получить информация по пользовтаелю по его идентификатору:
***
Доступно администратору и пользователю если информация отображается по нему
***
GET - http://localhost:8080/restaurant/api/users/{Идентификатор пользователя}
***
Входящие параметры:
- идентификатор пользователя по которому необходимо получить информацию
***
Результат:
- объект пользователя

#####Обновить информацию по пользователю:
***
Доступно администратору и пользователю если информация отображается по нему
***
PUT - http://localhost:8080/restaurant/api/users
***
Входящие параметры:
- объект пользователя, которого необходимо обновить
***
Результат:
- объект пользователя с обновленными данными

#####Удалить учетную запись пользователя:
***
Доступно администратору и пользователю если это его учетная запись
***
DELETE - http://localhost:8080/restaurant/api/users/{Идентификатор пользователя}
***
Входящие параметры:
- идентификатор пользователя которого необходимо удалить
***
Результат:
отсутствует
***
Статус ответа:
- 204

#####Изменить пароль от учетной записи
***
Доступно администратору и пользователю если это его учетная запись
***
PUT - http://localhost:8080/restaurant/api/users/{Идентификатор пользователя}/password
***
Входящие параметры:
- идентификатор пользователя которого необходимо удалить
- пароль старый (для администратора - не нужно)
- новый пароль
***
Результат:
отсутствует
***
Статус ответа:
- 204

###Работа с ресторанами

#####Получить перечень всех ресторанов с меню:
***
Доступно администратору и пользователю
***
GET - http://localhost:8080/restaurant/api/restaurants
***
Входящие параметры:
* (не обязательный параметр) jsonSort - сортировка
***
Результат:
- массив объектов ресторанов, зарегистрированных в системе

#####Получить информацию по одному ресторану с меню по его идентификатору:
***
Доступно администратору и пользователю и не авторизированному пользователю
***
GET - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}
***
Входящие параметры:
- идентификатор ресторана по которому необходимо получить информацию
***
Результат:
- объект ресторана с его меню

#####Создать новый ресторан:
***
Доступно администратору
***
POST - http://localhost:8080/restaurant/api/restaurants
***
Входящие параметры:
- объект ресторана, которого необходимо создать
***
Результат:
- объект ресторана, который был создан системой
***
Статус ответа:
- 201

#####Изменить данные по ресторану(в том числе меню)
***
Доступно администратору
***
PUT - http://localhost:8080/restaurant/api/restaurants
***
Входящие параметры:
* (не обязательный параметр) updateMenu - true/false обновлять ли меню
- объект ресторана, который необходимо обновить, может содержать меню.
***
Результат:
- объект ресторана, который был обновлен системой

#####Удалить ресторан по его идентификатору:
***
Доступно администратору и пользователю
***
DELETE - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}
***
Входящие параметры:
- идентификатор ресторана которого необходимо удалить
***
Результат:
отсутствует
***
Статус ответа:
- 204

###Работа с голосованием

#####Проголосовать за ресторан.:
***
Доступно пользователю
***
POST - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}/voting
***
Входящие параметры:
- идентификатор ресторана за который пользователь голосует
***
Результат:
отсутствует
***
Статус ответа:
- 204

#####Просмотреть рейтинг ресторанов на текущий/заданный день:
***
Доступно администратору и пользователю и не авторизированному пользователю
***
GET - http://localhost:8080/restaurant/api/restaurants/voting/{Дата рейтинга}
***
Входящие параметры:
* (не обязательный параметр) дата рейтинга в формате yyyy-mm-dd
***
Результат:
- массив объектов ресторанов с меню и их место в рейтинге