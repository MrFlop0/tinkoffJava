--liquibase formatted sql
--changeset 1:1
create sequence if not exists link_id_gen;
create table if not exists link
(
    link_id bigint default nextval('link_id_gen') primary key,
    link text not null,
    type integer not null,
    update_date timestamp not null
)
