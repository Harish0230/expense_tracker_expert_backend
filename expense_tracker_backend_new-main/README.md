# ExpenseTracker Expert - Backend

A production-ready Expense Tracker backend built with Spring Boot, Spring Security, JWT Authentication, MySQL, and REST APIs.

## Features

### Authentication & Authorization
- User Registration
- User Login
- JWT Authentication
- Refresh Token Support
- Password Encryption using BCrypt
- Protected APIs with Spring Security

### Dashboard
- Total Income
- Total Expenses
- Remaining Balance
- Savings Rate
- Monthly Budget Usage
- Recent Transactions
- Expense Category Breakdown

### Income Management
- Add Income
- Update Income
- Delete Income
- Search Income
- Filter Income

### Expense Management
- Add Expense
- Update Expense
- Delete Expense
- Category-based Tracking
- Search & Filter

### Budget Management
- Monthly Budget Creation
- Budget Allocation
- Budget Utilization Tracking
- Budget Alerts
- Remaining Budget Calculation

### Notifications
- Budget Exceeded Alerts
- Budget Warning Alerts
- Monthly Summary Notifications

### Reports & Analytics
- Income vs Expense Summary
- Monthly Spending Analysis
- Category-wise Expense Breakdown

---

## Tech Stack

### Backend
- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- JWT Authentication
- Maven

### Database
- MySQL

### Documentation
- Swagger OpenAPI 3

---

## Project Structure

```text
src/main/java/com/ameena/expensetracker

├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── exception
└── config
```

---

## Database Schema

### Users

| Column | Type |
|----------|----------|
| id | BIGINT |
| name | VARCHAR |
| email | VARCHAR |
| phone | VARCHAR |
| password | VARCHAR |

### Transactions

| Column | Type |
|----------|----------|
| id | BIGINT |
| title | VARCHAR |
| amount | DECIMAL |
| type | income/expense |
| category_id | BIGINT |
| date | DATE |
| notes | TEXT |
| user_id | BIGINT |

### Categories

| Column | Type |
|----------|----------|
| id | BIGINT |
| name | VARCHAR |
| color | VARCHAR |
| icon | VARCHAR |

### Budgets

| Column | Type |
|----------|----------|
| id | BIGINT |
| expense_limit | DECIMAL |
| income_target | DECIMAL |
| savings_goal | DECIMAL |
| user_id | BIGINT |

---

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI Docs:

```text
http://localhost:8080/v3/api-docs
```

---

## Local Setup

### Clone Repository

```bash
git clone https://github.com/Ameenajabeen/expense_tracker_backend_new.git
cd expense-tracker
```

### Configure Database

Update application.yml:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/expense_tracker
    username: root
    password: your_password
```

### Build Project

```bash
mvn clean install
```

### Run Application

```bash
mvn spring-boot:run
```

Application starts on:

```text
http://localhost:8080
```

---

## Railway Deployment

### Environment Variables

```env
SPRING_PROFILES_ACTIVE=prod

SPRING_DATASOURCE_URL=jdbc:mysql://<HOST>:<PORT>/<DATABASE>

SPRING_DATASOURCE_USERNAME=<USERNAME>

SPRING_DATASOURCE_PASSWORD=<PASSWORD>

JWT_SECRET=<YOUR_SECRET>
```

### Build Command

```bash
mvn clean package
```

### Start Command

```bash
java -jar target/expense-tracker-0.0.1-SNAPSHOT.jar
```

---

## Security Features

- JWT Access Token
- Refresh Token
- Password Encryption
- Protected REST APIs
- Spring Security Configuration
- Role-based Authorization Ready

---

## Future Enhancements

- AI Financial Insights
- Receipt Scanner (OCR)
- Email Reports
- Recurring Transactions
- Monthly Spending Target Tracking
- Admin Dashboard
- Multi-user Roles
- Expense Forecasting

---

## Author

**M. Ameena Jabeen**

B.Tech Information Technology  
R.M.D Engineering College

LinkedIn:https://www.linkedin.com/in/ameenajabeenmohammed2930/
GitHub:https://github.com/Ameenajabeen

---

## License

This project is created for educational and portfolio purposes.
