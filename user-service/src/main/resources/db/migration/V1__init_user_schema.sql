CREATE TABLE IF NOT EXISTS users (
   id serial,
   name TEXT NOT NULL,
   created_at BIGINT NOT NULL,
   updated_at BIGINT NOT NULL
);
