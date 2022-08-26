drop table if exists Book CASCADE;
create table Book (
       id bigint generated by default as identity,
        author varchar(20) not null,
        title varchar(50) not null,
        primary key (id)
    );