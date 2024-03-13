--liquibase formatted sql
--changeset 1:1
create table if not exists chat
(
    chat_id  bigint primary key
)
