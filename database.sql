CREATE DATABASE bank;

USE bank;

CREATE TABLE customers (
    CustomerId int PRIMARY KEY AUTO_INCREMENT,
    CustomerName VARCHAR(50),
    CustomerPassword VARCHAR(100)
);

CREATE TABLE currentaccounts (
    CurrentAccountId int PRIMARY KEY AUTO_INCREMENT,
    CustomerId int,
    CurrentAccountBalance DOUBLE,
    CurrentAccountName VARCHAR(50),
    CurrentAccountDescription VARCHAR(100),
    FOREIGN KEY (CustomerId) REFERENCES customers(CustomerId) ON DELETE CASCADE
);

CREATE TABLE isaaccounts (
    IsaAccountId int PRIMARY KEY AUTO_INCREMENT,
    CustomerId int,
    IsaAccountBalance DOUBLE,
    IsaAccountName VARCHAR(50),
    IsaAccountDescription VARCHAR(100),
    FOREIGN KEY (CustomerId) REFERENCES customers(CustomerId) ON DELETE CASCADE
);

CREATE TABLE savingsaccounts (
    SavingAccountId int PRIMARY KEY AUTO_INCREMENT,
    CustomerId int,
    SavingsAccountBalance DOUBLE,
    SavingsAccountName VARCHAR(50),
    SavingsAccountDescription VARCHAR(100),
    FOREIGN KEY (CustomerId) REFERENCES customers(CustomerId) ON DELETE CASCADE
);

INSERT INTO customers (
CustomerId, CustomerName,
CustomerPassword)
VALUES
-- password for Luke is password
('3', 'Luke Cahill', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g='),
-- password for this is $iege
('4', 'Your name', 'xBsFafWR65UVBCBCn2Chr27UMlc=');

INSERT INTO currentaccounts (
CurrentAccountId, CustomerId,
CurrentAccountBalance, CurrentAccountName, CurrentAccountDescription)
VALUES
(NULL, '3', '200.00', 'Lukes account', 'This is Luke Cahills current account.'),
(NULL, '4', '400.00', 'Other account', 'This is another persons current account.');

INSERT INTO isaaccounts (
IsaAccountId, CustomerId,
IsaAccountBalance, IsaAccountName, IsaAccountDescription)
VALUES
(NULL, '3', '10000.00', 'Lukes ISA', 'This is Luke Cahills ISA account.'),
(NULL, '4', '1000.00', 'Other ISA', 'This is some other persons ISA account.');
