--liquibase formatted sql

--changeset mzhikha:14
alter table stock_candles_one_hours RENAME TO stock_candles;
alter table stock_candles
    add column type varchar not null;
create table macd_indicator_info
(
    id               uuid primary key,
    value            numeric,
    date_time_period timestamptz,
    short_bar_count  integer,
    long_bar_count   integer,
    type             varchar,
    stock_candles_id uuid references stock_candles (id)
);
drop table stock_candles_two_hours;
drop table stock_candles_four_hours;
drop table stock_candles_one_day;





