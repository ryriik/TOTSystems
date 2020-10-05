create table securities (
    secid varchar(255) primary key,
    reg_number varchar(255),
    name varchar(255),
    isin varchar(255),
    emitent_title varchar(255)
);

create table history (
    id bigint primary key generated always as identity,
    boardid varchar(255),
    secid varchar(255),
    short_name varchar(255),
    trade_date date,
    num_trades float8,
    value float8,
    volume float8,
    open float8,
    close float8,
    low float8,
    high float8,
    constraint secid_fk foreign key (secid) references securities on delete cascade,
    constraint record_unique unique (boardid, secid, trade_date)
);
