ALTER TABLE tasks
ADD COLUMN user_id INT REFERENCES todo_user(id);