-- drops existing database
DROP DATABASE IF EXISTS web3bottle;

-- create and use new database
CREATE DATABASE web3bottle;

USE web3bottle;

-- create tables
CREATE TABLE users(
    public_address VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (public_address)
);

CREATE TABLE messages(
    msg_id INT AUTO_INCREMENT,
    msg VARCHAR(140) NOT NULL,
    public_address VARCHAR(255),
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY(msg_id),
    CONSTRAINT fk_address FOREIGN KEY (public_address) REFERENCES users(public_address)
);