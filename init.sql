CREATE SCHEMA temp;

CREATE TABLE temp.auth (
    id SERIAL primary key,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE temp.token (
    id SERIAL PRIMARY KEY,
    auth_id int NOT NULL,
    token UUID NOT null default gen_random_uuid(),
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (auth_id) REFERENCES temp.auth(id)
);

-- Auth 테이블 더미 데이터
INSERT INTO temp.auth (email, password)
VALUES
  ('test1@example.com', 'password1'),
  ('test2@example.com', 'password2'),
  ('test3@example.com', 'password3');
