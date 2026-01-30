USE bankdb;

CREATE TABLE customers (
    customer_id      VARCHAR(50) PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    email            VARCHAR(100),
    phone            VARCHAR(20),
    created_at       TIMESTAMP NOT NULL
);

CREATE TABLE accounts (
    account_number   VARCHAR(50) PRIMARY KEY,
    customer_id      VARCHAR(50) NOT NULL,
    account_type     VARCHAR(20) NOT NULL,  -- SAVINGS/CURRENT
    status           VARCHAR(20) NOT NULL,  -- ACTIVE/FROZEN/CLOSED
    balance          DOUBLE PRECISION NOT NULL,
    created_at       TIMESTAMP NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);


CREATE TABLE transactions (
    transaction_id   VARCHAR(50) PRIMARY KEY,
    account_number   VARCHAR(50) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,  -- CREDIT/DEBIT/TRANSFER/REVERSAL
    amount           DOUBLE PRECISION NOT NULL,
    success          BOOLEAN NOT NULL,
    failure_reason   VARCHAR(255),
    created_at       TIMESTAMP NOT NULL,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);