create table if not exists `cwl-web`.access_log
(
    id               int auto_increment
        primary key,
    access_record_id int                                not null,
    access_date      datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.access_record
(
    id                 int auto_increment
        primary key,
    ip_address         varchar(200) null,
    nick_name          varchar(200) null,
    access_time        datetime     null,
    last_access_time   datetime     null,
    access_count       int          null,
    last_watering_time datetime     null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.app
(
    id          int auto_increment
        primary key,
    name        varchar(100)       null,
    html        mediumtext         null,
    description varchar(300)       null,
    is_lock     int    default 0   not null,
    uses_times  bigint default 0   not null,
    icon_file   varchar(200)       null,
    color       varchar(100)       null,
    sort        int    default 999 null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.bad_words
(
    id   int auto_increment
        primary key,
    word varchar(200) null,
    type varchar(200) null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.blog
(
    id            int auto_increment
        primary key,
    title         text                               null,
    content       mediumtext                         null,
    release_time  datetime default CURRENT_TIMESTAMP null,
    update_time   datetime default CURRENT_TIMESTAMP null,
    tabs          varchar(500)                       null,
    short_content varchar(200)                       null,
    do_archive    int(1)   default 0                 not null,
    browser_times bigint   default 0                 not null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.blog_tabs
(
    id    int auto_increment
        primary key,
    name  varchar(200)       null,
    color varchar(200)       null,
    sort1 char default 'Z'   null,
    sort2 int  default 10000 null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.blog_tabs_subscribe
(
    id             int auto_increment
        primary key,
    email          varchar(100)                       not null,
    blog_tabs_id   int                                not null,
    subscribe_time datetime default CURRENT_TIMESTAMP not null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.chat_message
(
    id        int auto_increment
        primary key,
    content   text     null,
    send_time datetime null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.comment
(
    id                int auto_increment
        primary key,
    parent_id         int                                null,
    for_id            int                                null,
    blog_id           int                                null,
    nick_name         varchar(200)                       null,
    profile_url       varchar(1000)                      null,
    content           text                               null,
    send_time         datetime default CURRENT_TIMESTAMP null,
    email             varchar(200)                       null,
    website           varchar(200)                       null,
    ip_address        varchar(20)                        null,
    website_audit     int(1)   default 0                 null,
    verification_code varchar(64)                        null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.key_value_options
(
    opt_key   varchar(200) not null
        primary key,
    opt_value text         null,
    remark    varchar(200) null
)
    charset = utf8mb4;

create table if not exists `cwl-web`.oss_file
(
    oss_file_name    varchar(200) not null
        primary key,
    origin_file_name varchar(200) null,
    path             varchar(200) null,
    suffix           varchar(200) null,
    full_url         varchar(200) null
)
    charset = utf8mb4;

