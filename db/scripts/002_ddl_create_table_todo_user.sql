CREATE TABLE if not exists todo_user (
   id SERIAL PRIMARY KEY,
   name varchar NOT NULL,
   login varchar NOT NULL UNIQUE,
   password varchar NOT NULL
);