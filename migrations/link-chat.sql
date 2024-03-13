--liquibase formatted sql
--changeset 1:1
create table if not exists links_chats
(
    chat_id bigint,
    link_id bigint,
    primary key (chat_id, link_id),
    foreign key (chat_id) references chat (chat_id),
    foreign key (link_id) references link (link_id)
)
