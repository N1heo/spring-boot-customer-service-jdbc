CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,

    phone VARCHAR(30) NOT NULL UNIQUE,

    image_path TEXT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,

    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(50) NOT NULL,

    customer_id UUID NULL,

    CONSTRAINT fk_users_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
        ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_users_customer_id ON users(customer_id);
