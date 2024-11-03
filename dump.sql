CREATE DATABASE ecommerce_app;

USE ecommerce_app;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    wallet DECIMAL(10,2) DEFAULT 500.00,
    PRIMARY KEY (id)
);

CREATE TABLE products (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE orders (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO products (name, price, description) VALUES
('HP Laptop', 1000.00, 'A powerful laptop with 32GB RAM and 512GB SSD.'),
('Iphone', 500.00, 'A smartphone with a stunning display and great camera.'),
('Air pods', 150.00, 'Noise-cancellation and transparancy modes.'),
('Apple Watch', 200.00, 'A smartwatch with all features you need.'),
('I-Pad', 300.00, 'A tablet with easy note-taking capabilities.');