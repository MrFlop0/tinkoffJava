--liquibase formatted sql
--changeset 1:1
create table if not exists link
(
    link text not null primary key,
    type integer not null,
    stars_count integer,
    update_date timestamp not null,
    previous_check timestamp not null
)
