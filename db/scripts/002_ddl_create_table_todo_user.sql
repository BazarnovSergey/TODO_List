CREATE TABLE if not exists todo_user (
   id SERIAL PRIMARY KEY,
   name TEXT,
   login character UNIQUE,
   password TEXT
);