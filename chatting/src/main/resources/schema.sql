DROP TABLE IF EXISTS user_account;
DROP TABLE IF EXISTS chat_room;
DROP TABLE IF EXISTS chat_join;
DROP TABLE IF EXISTS topic;

CREATE TABLE user_account
(
    id                bigint       not null AUTO_INCREMENT,
    login_id          varchar(64)  not null,
    email             varchar(255) not null,
    password          varchar(255) not null,
    nickname          varchar(64)  not null,
    profile_url       varchar(255) not null,
    user_account_role varchar(64)  not null,
    created_at        timestamp    not null,
    modified_at       timestamp    not null,
    PRIMARY KEY (id)
);

CREATE TABLE chat_room
(
    id            bigint       not null AUTO_INCREMENT,
    name          varchar(64)  not null,
    thumbnail_url varchar(255) not null,
    max_count     int          not null,
    topic_id      bigint       not null,
    created_at    timestamp    not null,
    modified_at   timestamp    not null,
    PRIMARY KEY (id)
);

CREATE TABLE chat_join
(
    id              bigint    not null AUTO_INCREMENT,
    user_account_id bigint    not null,
    chat_room_id    bigint    not null,
    created_at      timestamp not null,
    modified_at     timestamp not null,
    PRIMARY KEY (id)
);

CREATE TABLE topic
(
    id          bigint      not null AUTO_INCREMENT,
    name        varchar(64) not null,
    created_at  timestamp   not null,
    modified_at timestamp   not null,
    PRIMARY KEY (id)
);
