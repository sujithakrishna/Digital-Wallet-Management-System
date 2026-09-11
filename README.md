# Digital Wallet Management System

A Java-based digital wallet management system developed using Core Java, JDBC, SQL, and MySQL. The project simulates common digital wallet operations such as user registration, wallet management, deposits, withdrawals, money transfers, beneficiary management, payment requests, transaction tracking, and audit logging.

## Overview

The Digital Wallet Management System is a console-based application designed to demonstrate backend development using Java and database connectivity.

The application follows a structured approach using Java classes for business logic, DAO classes for database operations, and MySQL for persistent data storage.

## Technologies Used

- Java
- Core Java
- JDBC
- SQL
- MySQL
- Eclipse IDE
- Git
- GitHub

## Key Features

### User Management
- User registration
- User login
- Profile management
- Password change
- Password validation
- User data retrieval

### Wallet Management
- Wallet creation
- Check wallet balance
- Deposit money
- Withdraw money
- Transfer money
- Lock wallet
- Unlock wallet

### Transaction Management
- Transaction creation
- Transaction history
- Transaction details
- Transaction status tracking
- Transaction cancellation
- Transaction search
- Mini statement
- Transaction statistics
- Transaction receipts

### Beneficiary Management
- Add beneficiary
- View beneficiaries
- Manage beneficiary details

### Payment Request Management
- Create payment requests
- View payment requests
- Manage payment request status

### Audit and Reliability
- Audit logging
- Idempotency handling
- Input validation
- Exception handling

## Project Structure

The project is organized into different classes based on their responsibilities.

```text
Digital_Wallet_Management_System
│
├── src
│   └── Digital_Wallet_Management_System
│       │
│       ├── User.java
│       ├── UserDAO.java
│       ├── Userstore.java
│       │
│       ├── Wallet.java
│       ├── WalletDAO.java
│       ├── WalletService.java
│       ├── WalletStore.java
│       │
│       ├── Transaction.java
│       ├── TransactionStore.java
│       ├── TransactionReceipt.java
│       ├── TransactionStatisticsService.java
│       ├── TransactionStatus.java
│       └── TransactionType.java
│       │
│       ├── Beneficiary.java
│       ├── BeneficiaryDAO.java
│       ├── BeneficiaryStore.java
│       │
│       ├── PaymentRequest.java
│       ├── PaymentRequestStore.java
│       │
│       ├── AuditLog.java
│       ├── AuditLogService.java
│       ├── AuditLogStore.java
│       │
│       ├── IdempotencyService.java
│       ├── IdempotencyStore.java
│       │
│       ├── DatabaseConnection.java
│       ├── PasswordValidator.java
│       ├── AmountFormatter.java
│       ├── ReceiptService.java
│       └── Menu.java
│
└── README.md
