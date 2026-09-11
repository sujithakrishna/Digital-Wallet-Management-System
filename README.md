# 💳 Digital Wallet Management System

A console-based digital wallet management application developed using **Core Java, JDBC, SQL, and MySQL**. The project is designed to simulate common digital wallet operations such as user registration, login, wallet management, deposits, withdrawals, money transfers, beneficiary management, payment requests, transaction tracking, receipts, and audit logging.

## 📌 Project Overview

The **Digital Wallet Management System** is a Java-based backend application that demonstrates how a database-driven application can be developed using Core Java and MySQL.

The application allows users to register and log in, manage their wallet, check their balance, deposit and withdraw money, transfer money using beneficiaries, manage payment requests, view transaction history, search transactions, and perform other wallet-related operations.

The application uses **JDBC (Java Database Connectivity)** to connect the Java application with a **MySQL database**. User, wallet, beneficiary, transaction, payment request, and audit information can be stored and retrieved from the database.

The project was developed to gain practical experience with **Core Java, Object-Oriented Programming, JDBC, SQL, MySQL, database connectivity, data validation, exception handling, transaction processing, and modular application development**.

## 🛠️ Technologies Used

- **Java**
- **Core Java**
- **Object-Oriented Programming**
- **JDBC**
- **SQL**
- **MySQL**
- **Eclipse IDE**
- **Git**
- **GitHub**

## ✨ Features

### 👤 User Management

- User registration
- User login
- View user profile
- Edit profile
- Change password
- User input validation
- Password validation
- Failed login attempt tracking
- Account locking

### 💰 Wallet Management

- Wallet creation
- Check wallet balance
- Deposit money
- Withdraw money
- Transfer money
- Lock wallet
- Unlock wallet
- Wallet status management
- Automatic balance updates after transactions

### 👥 Beneficiary Management

- Add beneficiary
- View beneficiary details
- Retrieve beneficiary information
- Manage beneficiary records
- Use registered beneficiaries for money transfers

### 💸 Transaction Management

- Deposit transactions
- Withdrawal transactions
- Transfer transactions
- Transaction ID generation
- Transaction status tracking
- Transaction date and time tracking
- Transaction history
- Mini statement
- Transaction details
- Transaction search
- Transaction cancellation
- Transaction statistics

### 🔄 Payment Request Management

- Create payment requests
- View payment requests
- Process payment requests
- Track payment request status

### 🧾 Transaction Receipt

The application provides transaction receipt information after successful transactions, including:

- Transaction ID
- Transaction type
- Transaction amount
- Transaction status
- Transaction date and time
- Available wallet balance

### 📝 Audit Logging

The application maintains audit information for important activities performed within the system.

### 🔁 Idempotency

The project includes basic idempotency handling for transaction-related operations to help prevent the same request from being processed repeatedly.

## 🏗️ Application Architecture

The application is organized into different components so that user management, wallet operations, transaction processing, and database operations remain separated.

```text
                  DIGITAL WALLET MANAGEMENT SYSTEM
                               |
                               v
                     Console Application
                               |
          +--------------------+--------------------+
          |                    |                    |
          v                    v                    v
     User Module         Wallet Module       Transaction Module
          |                    |                    |
          +--------------------+--------------------+
                               |
                               v
                       Service / DAO Layer
                               |
                               v
                             JDBC
                               |
                               v
                            MySQL
```

## 🔄 Application Flow

The general flow of a database operation is:

```text
User Input
    |
    v
Input Validation
    |
    v
Application / Business Logic
    |
    v
DAO / Store Operations
    |
    v
JDBC
    |
    v
MySQL Database
    |
    v
Operation Result
    |
    v
Console Output / Transaction Receipt
```

## 📋 Application Menu

The application provides the following main operations:

```text
1. Register User
2. Login
3. Edit Profile
4. Change Password
5. View Profile
6. Check Balance
7. Deposit Money
8. Withdraw Money
9. Transfer Money
10. Transaction History
11. Mini Statement
12. Transaction Details
13. Cancel Transaction
14. Search Transactions
15. Lock Wallet
16. Unlock Wallet
17. Beneficiary Management
18. Payment Request Management
19. Logout
20. Exit
21. Transaction Statistics
```

## 👤 User Registration

A new user can register by entering the required information such as:

* Full name
* Email address
* Mobile number
* Password
* Password confirmation

After successful registration, the user information is stored in the database and the required wallet information is created.

## 🔐 Login

Registered users can log in using their credentials.

The application validates the entered information against the stored user records before providing access to wallet operations.

The application also includes handling for failed login attempts and account locking.

## 💰 Wallet Operations

### Check Balance

Users can check the amount currently available in their wallet.

### Deposit Money

Users can deposit money into their wallet. After a successful deposit, the wallet balance is updated and the transaction is stored in the database.

### Withdraw Money

Users can withdraw money from their wallet. The application checks the available wallet balance before processing the withdrawal.

### Transfer Money

Users can transfer money using the beneficiary information available in the system. The transfer is processed and the transaction details are recorded.

## 👥 Beneficiary Management

The beneficiary module allows users to add and manage beneficiary information.

A beneficiary can be added with the required account details and can later be used when performing transfer operations.

Beneficiary information is stored in the MySQL database.

## 🔄 Payment Requests

The payment request module allows users to create and manage payment requests.

Users can:

* Create payment requests
* View payment requests
* Check payment request status
* Process payment requests

## 💸 Transaction Management

The transaction module handles different types of wallet transactions.

Supported transaction types include:

```text
DEPOSIT
WITHDRAWAL
TRANSFER
```

Transaction records can contain:

```text
Transaction ID
User / Wallet
Transaction Type
Amount
Transaction Status
Date and Time
Balance Information
```

## 📜 Transaction History

Users can view previously recorded transactions through the transaction history option.

Transaction history can include:

* Transaction ID
* Transaction type
* Transaction amount
* Transaction status
* Transaction date and time

## 📄 Mini Statement

The mini statement provides a summarized view of wallet transaction activity and can be used to quickly review recent transactions.

## 🔎 Transaction Search

The application provides functionality to search stored transaction records and retrieve specific transaction information.

## 📋 Transaction Details

Users can retrieve detailed information about a particular transaction, including its transaction ID, type, amount, status, date, and related wallet information.

## ❌ Transaction Cancellation

The application provides functionality for cancelling eligible transactions according to the transaction state and business rules implemented in the application.

## 📊 Transaction Statistics

The application provides transaction statistics based on the transaction records stored in the database.

This allows users to review transaction-related activity within the wallet.

## 🔒 Wallet Lock and Unlock

Users can lock their wallet when required.

A locked wallet can restrict wallet-related operations until it is unlocked.

The application also provides an option to unlock the wallet.

## 📝 Audit Logging

Important application activities can be recorded through the audit logging functionality.

Audit information helps maintain records of operations performed within the system.

## 🔁 Idempotency

The project includes basic idempotency handling for transaction-related operations.

This helps prevent the same operation from being processed repeatedly when the same request is encountered.

## 🧾 Transaction Receipt

After a successful transaction, the application can display transaction receipt information.

Example:

```text
--------------------------------------------
              TRANSACTION RECEIPT
--------------------------------------------
Transaction ID   : TXN1001
Transaction Type : DEPOSIT
Amount           : ₹1,000
Status           : SUCCESS
Date & Time      : 09-09-2026
Balance          : ₹5,000
--------------------------------------------
```

## 🔌 JDBC Database Connectivity

The application uses **JDBC** to establish communication between Java and MySQL.

The connection follows this flow:

```text
Java Application
       |
       v
      JDBC
       |
       v
     MySQL
       |
       v
    Database
```

The application uses JDBC components such as:

* `Connection`
* `DriverManager`
* `PreparedStatement`
* `ResultSet`
* `SQLException`

## 🗄️ Database

The project uses **MySQL** as the relational database for persistent storage.

The database stores information related to:

* Users
* Wallets
* Transactions
* Beneficiaries
* Payment Requests
* Audit Logs

### Database Name

```text
digital_wallet
```

## 🧠 Core Java Concepts Used

The project demonstrates practical usage of:

* Classes and Objects
* Encapsulation
* Abstraction
* Inheritance
* Polymorphism
* Interfaces
* Constructors
* Methods
* Collections
* Exception Handling
* Custom Exceptions
* Conditional Statements
* Loops
* String Handling
* Input Validation
* Enums
* JDBC
* PreparedStatement
* ResultSet
* Database Connectivity
* DAO Pattern
* Service-based application structure
* SQL Queries
* Transaction Processing

## 🗃️ Database Concepts Used

The project provides practical experience with:

* Database creation
* Table creation
* Primary Keys
* Foreign Keys
* Relationships
* SQL Queries
* `INSERT`
* `SELECT`
* `UPDATE`
* `DELETE`
* `WHERE` conditions
* `JOIN` operations
* Database normalization concepts
* JDBC connectivity
* Persistent data storage

## 📂 Project Structure

```text
Digital-Wallet-Management-System/
│
├── src/
│   └── Digital_Wallet_Management_System/
│       ├── User related classes
│       ├── Wallet related classes
│       ├── Transaction related classes
│       ├── Beneficiary related classes
│       ├── Payment Request related classes
│       ├── Receipt related classes
│       ├── Audit Log related classes
│       ├── Validation classes
│       ├── Service classes
│       ├── Store / DAO classes
│       └── Database connection class
│
├── .gitignore
├── .classpath
├── .project
└── README.md
```

## 🚀 How to Run the Project

### Step 1: Clone the Repository

Open a terminal and run:

```bash
git clone https://github.com/sujithakrishna/Digital-Wallet-Management-System.git
```

Then open the project directory:

```bash
cd Digital-Wallet-Management-System
```

### Step 2: Open the Project in Eclipse

Open **Eclipse IDE** and import the project as a Java project.

Make sure the project is recognized correctly and that the Java source files are available under the `src` folder.

### Step 3: Install and Start MySQL

Make sure MySQL Server is installed and running.

MySQL Workbench can be used to create and manage the database.

### Step 4: Create the Database

Open MySQL Workbench or MySQL Command Line and execute:

```sql
CREATE DATABASE digital_wallet;
```

Select the database:

```sql
USE digital_wallet;
```

### Step 5: Create the Required Tables

Create the tables required by the application.

The database requires tables for:

```text
Users
Wallets
Transactions
Beneficiaries
Payment Requests
Audit Logs
```

The table structure should match the SQL queries and database operations implemented in the Java project.

### Step 6: Configure the Database Connection

Open the database connection class and configure the local MySQL details.

Example:

```java
String url = "jdbc:mysql://localhost:3306/digital_wallet";
String username = "root";
String password = "your_password";
```

Replace `your_password` with the password configured for the local MySQL installation.

Do not commit actual database passwords or credentials to GitHub.

### Step 7: Add MySQL Connector/J

Because this project is a normal Java project rather than a Maven project, the MySQL JDBC driver must be added manually to the Eclipse Build Path.

In Eclipse:

```text
Right-click Project
→ Build Path
→ Configure Build Path
→ Libraries
→ Classpath
→ Add External JARs
```

Select the MySQL Connector/J `.jar` file and apply the changes.

### Step 8: Run the Application

Open the main Java class in Eclipse.

Right-click the class and select:

```text
Run As → Java Application
```

The application will start in the Eclipse Console.

## 🧪 Testing

The application was tested through different user and wallet workflows, including:

* User registration
* User login
* Invalid login attempts
* Profile updates
* Password changes
* Wallet creation
* Balance checking
* Deposit operations
* Withdrawal operations
* Money transfers
* Beneficiary management
* Payment requests
* Transaction history
* Transaction details
* Transaction search
* Transaction cancellation
* Wallet locking and unlocking
* Transaction statistics
* Database persistence

## 📚 Learning Outcomes

This project provided practical experience in developing a database-driven Java application.

The major areas practiced include:

* Core Java programming
* Object-Oriented Programming
* JDBC database connectivity
* MySQL database operations
* SQL query writing
* PreparedStatement and ResultSet
* DAO and Store-based database operations
* Application validation
* Exception handling
* Transaction processing
* Wallet balance management
* Database persistence
* Debugging Java and SQL issues
* Git and GitHub version control

## 🎯 Project Objective

The main objective of this project is to develop a practical Java backend application that demonstrates how a digital wallet system can be implemented using **Core Java, JDBC, SQL, and MySQL**.

The project focuses on:

* Java backend development
* Object-Oriented Programming
* Database integration
* SQL operations
* Transaction processing
* Data validation
* Exception handling
* Persistent data management
* Modular application development

## 🚧 Current Scope

The current version of the project is a **console-based Java application with MySQL database integration**.

The project focuses on backend logic, database operations, wallet management, transaction processing, validation, and persistent data storage.

## 🔮 Future Improvements

Possible future improvements include:

* Web-based user interface
* Spring Boot integration
* REST API development
* Improved authentication and authorization
* Password hashing
* Automated unit and integration testing
* Maven or Gradle build management
* Logging framework integration
* Improved transaction concurrency handling
* Docker support
* Cloud deployment
* Administrative dashboard
* Enhanced reporting and analytics

## 🔗 GitHub Repository

[https://github.com/sujithakrishna/Digital-Wallet-Management-System](https://github.com/sujithakrishna/Digital-Wallet-Management-System)

## 👩‍💻 Author

**Sujitha V K**

Java Developer | Backend Development | SQL

GitHub: [https://github.com/sujithakrishna](https://github.com/sujithakrishna)

## 📄 Disclaimer

This project is developed for **learning, educational, and portfolio purposes**.

It is not intended to process real financial transactions or store actual banking or customer financial information.
