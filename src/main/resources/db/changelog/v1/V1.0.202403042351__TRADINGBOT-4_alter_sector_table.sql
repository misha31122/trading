--liquibase formatted sql

--changeset mzhikha:8
ALTER TABLE sectors_info
    drop COLUMN sub_industry_id;
--rollback ALTER TABLE stock add COLUMN sub_industry_id integer;

--changeset mzhikha:9
update stock set sectors_info_id = NULL;