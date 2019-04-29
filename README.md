# Итоговый проект по Java Enterprise Online Project

#REST-API:

Получить перечень всех пользователей. Доступно только администратору:

GET - http://localhost:8080/restaurant/api/users

Получить информация по пользовтаелю по его идентификатору. Доступно администратору и пользователю если информация 
отображается по нему:

GET - http://localhost:8080/restaurant/api/users/{Идентификатор пользователя}

Создать нового пользователя. Доступно по функционалу создания новой учетной записи с клиентской части:

POST - http://localhost:8080/restaurant/api/users

@RequestBody UserDTO

Обновить информацию по пользователю. Доступно администратору и пользователю если информация отображается по нему:

PUT - http://localhost:8080/restaurant/api/users

Удалить учетную запись пользователя. Доступно администратору и пользователю если это его учетная запись:

DELETE - http://localhost:8080/restaurant/api/users/{Идентификатор пользователя}

Получить перечень всех ресторанов с меню:

GET - http://localhost:8080/restaurant/api/restaurants

Получить информацию по одному ресторану с меню:

GET - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}

Создать новый ресторан. Доступно администратору:

POST - http://localhost:8080/restaurant/api/restaurants

Обновить информацию об ресторане (включая меню). Если перечень меню = null обновляем только информацию по ресторану не
трогая меню. Доступно администратору:

PUT - http://localhost:8080/restaurant/api/restaurants

Удалить ресторан. Доступно администратору:

DELETE - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}

Проголосовать за ресторан. Доступ только пользовтаели:

POST - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}/voting

Просмотреть рейтинг ресторанов на текущий день

GET - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}/voting

Просмотреть рейтинг ресторанов на заданный день

GET - http://localhost:8080/restaurant/api/restaurants/{Идентификатор ресторана}/voting/{Дата в формате}