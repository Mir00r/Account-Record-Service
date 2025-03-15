# Account Record Service

A Spring Boot application that processes account records from a text file using Spring Batch and provides RESTful APIs to retrieve and update the records.

## Features

- Spring Batch job to import account records from a text file
- RESTful APIs with JWT authentication
- Pagination support for all retrieval APIs
- Search functionality by customer ID, account number(s), or description
- Concurrent update handling using optimistic locking

## Technologies Used

- Java 11
- Spring Boot 2.7.x
- Spring Batch
- Spring Security with JWT
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- Swagger/OpenAPI for API documentation

## Setup Instructions

### Prerequisites

- Java 11 or higher
- Maven

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Maven:

```bash
mvn spring-boot:run
```

4. The application will start on port 8080
5. Access the H2 console at http://localhost:8080/h2-console
6. Access the Swagger UI at http://localhost:8080/swagger-ui.html

## API Documentation

### Authentication APIs

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Account APIs

- `GET /api/accounts` - Get all accounts (paginated)
- `GET /api/accounts/{accountNumber}` - Get account by account number
- `GET /api/accounts/by-customer/{customerId}` - Get accounts by customer ID (paginated)
- `GET /api/accounts/by-account-numbers` - Get accounts by a list of account numbers (paginated)
- `GET /api/accounts/by-description` - Get accounts by description (paginated)
- `PUT /api/accounts/{accountNumber}` - Update account description

## Design Patterns Used

1. **Repository Pattern**: Used for data access abstraction with Spring Data JPA