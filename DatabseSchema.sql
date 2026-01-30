CREATE DATABASE IF NOT EXISTS bankdb_org;
USE bankdb_org;
CREATE TABLE customers (
    customer_id VARCHAR(50) PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    pan         VARCHAR(20)  NOT NULL
);

CREATE TABLE accounts (
    account_number   VARCHAR(50) PRIMARY KEY,
    customer_id      VARCHAR(50) NOT NULL,
    account_type     VARCHAR(20) NOT NULL,
    status           VARCHAR(20) NOT NULL,
    balance          DOUBLE      NOT NULL,
    minimum_balance  DOUBLE      NOT NULL,
    interest_rate    DOUBLE,          -- for savings
    overdraft_limit  DOUBLE,          -- for current
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE transactions (
    transaction_id        VARCHAR(50) PRIMARY KEY,
    account_number        VARCHAR(50) NOT NULL,
    transaction_type      VARCHAR(20) NOT NULL,
    amount                DOUBLE      NOT NULL,
    timestamp             TIMESTAMP   NOT NULL,
    success               BOOLEAN     NOT NULL,
    failure_reason        VARCHAR(255),
    related_transaction_id VARCHAR(50),
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
SELECT * FROM customers;