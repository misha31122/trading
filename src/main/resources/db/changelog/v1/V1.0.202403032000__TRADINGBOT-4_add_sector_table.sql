--liquibase formatted sql

--changeset mzhikha:6
create table sectors_info
(
    id                uuid primary key,
    industry          varchar(300),
    industry_group    varchar(300),
    industry_group_id integer,
    industry_id       integer,
    sector            varchar(200),
    sector_id         integer,
    sub_industry_id   integer
);
--rollback drop table sectors_info;

--changeset mzhikha:7
ALTER TABLE stock
    ADD COLUMN sectors_info_id uuid references sectors_info(id);
--rollback ALTER TABLE stock drop COLUMN sectors_info_id;