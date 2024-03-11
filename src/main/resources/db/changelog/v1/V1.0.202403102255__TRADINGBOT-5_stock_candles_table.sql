--liquibase formatted sql

--changeset mzhikha:10
ALTER TABLE stock_candles_one_hours
drop
COLUMN name;
--rollback ALTER TABLE stock_candles_one_hours add COLUMN name varchar(255);

--changeset mzhikha:11
ALTER TABLE stock_candles_two_hours
drop
COLUMN name;
--rollback ALTER TABLE stock_candles_two_hours add COLUMN name varchar(255);

--changeset mzhikha:12
ALTER TABLE stock_candles_four_hours
drop
COLUMN name;
--rollback ALTER TABLE stock_candles_four_hours add COLUMN name varchar(255);

--changeset mzhikha:13
ALTER TABLE stock_candles_one_day
drop
COLUMN name;
--rollback ALTER TABLE stock_candles_one_day add COLUMN name varchar(255);

