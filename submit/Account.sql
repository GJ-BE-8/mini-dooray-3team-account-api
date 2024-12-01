create table account(
    account_id bigint auto_increment primary key,
    created_at datetime(6)                           not null,
    updated_at datetime(6)                           null,
    email      varchar(255)                          not null unique,
    password   varchar(255)                          not null,
    username   varchar(255)                          not null,
    role       enum ('ADMIN', 'MEMBER')              not null,
    status     enum ('ACTIVE', 'DELETED', 'DORMANT') not null
);
