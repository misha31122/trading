--liquibase formatted sql

--changeset mzhikha:1
create table stock
(
    id         uuid primary key,
    name       varchar(255),
    figi       varchar(20) not null,
    share_data jsonb       not null

);
--rollback drop table stock;

--changeset mzhikha:2
create table stock_candles_two_hours
(
    id        uuid primary key,
    name      varchar(255),
    stock_id  uuid references stock (id) not null,
    candle    jsonb                      not null,
    date_time timestamptz                not null
);
create index idx_stock_id_two_hours
    on stock_candles_two_hours (stock_id);
--rollback drop table stock_candles_two_hours;

--changeset mzhikha:3
create table stock_candles_four_hours
(
    id        uuid primary key,
    name      varchar(255),
    stock_id  uuid references stock (id) not null,
    candle    jsonb                      not null,
    date_time timestamptz                not null
);
create index idx_stock_id_four_hours
    on stock_candles_four_hours (stock_id);
--rollback drop table stock_candles_four_hours;

--changeset mzhikha:4
create table stock_candles_one_day
(
    id        uuid primary key,
    name      varchar(255),
    stock_id  uuid references stock (id) not null,
    candle    jsonb                      not null,
    date_time timestamptz                not null
);
create index idx_stock_id_one_day
    on stock_candles_one_day (stock_id);
--rollback drop table stock_candles_one_day;

--changeset mzhikha:5
create table stock_candles_one_hours
(
    id        uuid primary key,
    name      varchar(255),
    stock_id  uuid references stock (id) not null,
    candle    jsonb                      not null,
    date_time timestamptz                not null
);
create index idx_stock_id_one_hours
    on stock_candles_one_hours (stock_id);
--rollback drop table stock_candles_one_hours;