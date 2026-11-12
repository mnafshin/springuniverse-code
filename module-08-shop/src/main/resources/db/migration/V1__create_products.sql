CREATE TABLE products (
    id   BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

INSERT INTO products (id, name) VALUES (1, 'Coffee');
INSERT INTO products (id, name) VALUES (2, 'Tea');
