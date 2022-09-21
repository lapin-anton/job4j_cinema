CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR NOT NULL,
  email VARCHAR NOT NULL UNIQUE,
  phone VARCHAR NOT NULL UNIQUE
);

CREATE TABLE sessions (
  id SERIAL PRIMARY KEY,
  name text
  photo BYTEA
);

CREATE TABLE ticket (
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL REFERENCES sessions(id) UNIQUE,
    pos_row INT NOT NULL UNIQUE,
    cell INT NOT NULL UNIQUE,
    user_id INT NOT NULL REFERENCES users(id)
);