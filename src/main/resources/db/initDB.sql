create table RESTAURANT
(
  ID            INTEGER      not null,
  NAME          VARCHAR(500) not null,
  PHONE         VARCHAR(100),
  ADDRESS       VARCHAR(2000),
  NOTE          VARCHAR(2000),
  EMAIL         VARCHAR(500),
  TIME_WORK     VARCHAR(1000),
  CREATE_DATE   TIMESTAMP,
  MODIFIED_DATE TIMESTAMP,
  constraint RESTAURANT__PK
  primary key (ID)
);

comment on table RESTAURANT
is 'Ресторан';

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
  MODIFIED_DATE TIMESTAMP,
  constraint MENU__PK
  primary key (ID),
  constraint FKBLWDTXEVPL4MRDS8A12Q0OHU6
  foreign key (RESTAURANT_ID) references RESTAURANT,
  constraint MENU_RESTAURANT_ID_FK
  foreign key (RESTAURANT_ID) references RESTAURANT
  on update cascade on delete cascade
);

comment on table MENU
is 'Меню';

create table USER
(
  ID               INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_5913B4FF_D458_491E_B8A1_8E9ADAAE7102),
  LOGIN            VARCHAR(500)  not null,
  PASSWORD         VARCHAR(4000) not null,
  FIRST_NAME       VARCHAR(500),
  LAST_NAME        VARCHAR(500),
  PATRONYMIC       VARCHAR(500),
  CREATE_DATE      TIMESTAMP,
  MODIFIED_DATE    TIMESTAMP,
  SECURITY_ROLE_ID INTEGER,
  constraint USER__PK
  primary key (ID)
);

comment on table USER
is 'Пользователь';

create table VOTING
(
  ID            INTEGER default (NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE_D2CD4B47_0643_4B9E_9BF1_393ECBD22B56)
    primary key,
  RESTAURANT_ID INTEGER not null,
  USER_ID       INTEGER,
  CREATE_DATE   TIMESTAMP,
  MODIFIED_DATE TIMESTAMP,
  REPORT_DATE   DATE    not null,
  constraint VOTING_RESTAURANT_ID_FK
  foreign key (RESTAURANT_ID) references RESTAURANT
  on update cascade on delete cascade,
  constraint VOTING_USER_ID_FK
  foreign key (USER_ID) references USER
  on update cascade on delete set null
);

comment on table VOTING
is 'Голосование пользователей за рестораны';