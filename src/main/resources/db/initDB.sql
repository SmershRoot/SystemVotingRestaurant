create table USER
(
  ID         INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_5913B4FF_D458_491E_B8A1_8E9ADAAE7102),
  LOGIN      VARCHAR(500)  not null,
  PASSWORD   VARCHAR(4000) not null,
  FIRST_NAME VARCHAR(500),
  LAST_NAME  VARCHAR(500),
  PATRONYMIC VARCHAR(500),
  CREATE_DATE DATETIME,
  MODIFIED_DATE DATETIME,
  constraint USER__PK
  primary key (ID)
);

comment on table USER
is 'Пользователь';

CREATE TABLE RESTAURANT
(
    ID int NOT NULL,
    NAME VARCHAR2(500) NOT NULL,
    PHONE VARCHAR2(100),
    ADDRESS VARCHAR2(2000),
    NOTE VARCHAR2(2000),
    EMAIL VARCHAR2(500),
    TIME_WORK VARCHAR2(1000),
    CREATE_DATE DATETIME,
    MODIFIED_DATE DATETIME,
    constraint RESTAURANT__PK
    primary key (ID)
);
COMMENT ON TABLE RESTAURANT IS 'Ресторан'

create table MENU
(
  ID            INTEGER      not null,
  NAME          VARCHAR(200) not null,
  NOTE          VARCHAR(1000),
  PHOTO_SRC     VARCHAR(4000),
  DATE_MENU     DATE         not null,
  RESTAURANT_ID INTEGER      not null,
  CREATE_DATE   TIMESTAMP,
  MODIFY_DATE   TIMESTAMP,
  constraint MENU__PK
  primary key (ID),
  constraint MENU_RESTAURANT_ID_FK
  foreign key (RESTAURANT_ID) references RESTAURANT
  on update cascade on delete cascade
);

comment on table MENU
is 'Меню';