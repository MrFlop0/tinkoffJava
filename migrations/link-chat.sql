--liquibase formatted sql
--changeset 1:1
create table if not exists links_chats
(
    chat_id bigint,
    link text,
    primary key (chat_id, link),
    foreign key (chat_id) references chat (chat_id),
    foreign key (link) references link (link)
)
