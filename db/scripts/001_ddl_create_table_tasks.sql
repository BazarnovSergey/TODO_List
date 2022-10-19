CREATE TABLE if not exists tasks (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created TIMESTAMP,
   done BOOLEAN
);