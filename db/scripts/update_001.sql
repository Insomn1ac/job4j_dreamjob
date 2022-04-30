CREATE TABLE IF NOT EXISTS post (
    id serial primary key,
    name text,
    description text,
    created text,
    visible boolean,
    city_id integer
);