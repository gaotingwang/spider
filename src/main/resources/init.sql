create schema spider_dev collate utf8mb4_general_ci;

create table t_fucai
(
    id bigint auto_increment,
    code varchar(255) not null,
    date varchar(255) not null,
    week varchar(255) not null,
    red varchar(255) not null,
    blue varchar(255) null,
    constraint t_fucai_pk
        primary key (id)
);
