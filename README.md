# Account Record Service

A robust Spring Boot application that processes account records from a text file using Spring Batch and provides secure RESTful APIs to retrieve and update the records. The application implements industry-standard design patterns and best practices to ensure maintainability, scalability, and security.

## Features

- Batch processing of account records from text files using Spring Batch
- Secure RESTful APIs with JWT authentication
- Advanced pagination support for efficient data retrieval
- Flexible search functionality by customer ID, account number(s), or description
- Concurrent update handling with optimistic locking
- Comprehensive API documentation with Swagger/OpenAPI
- In-memory H2 database for easy testing and development

## Architecture & Design Patterns

The application follows a layered architecture with clear separation of concerns and implements several design patterns:

1. **Repository Pattern**
   - Implements data access abstraction using Spring Data JPA
   - Provides clean separation between domain logic and data access logic
   - Enables easy switching between different data sources

2. **Builder Pattern**
   - Used in DTOs (AccountDTO, AuthDTO) for flexible object construction
   - Ensures immutability and validates object state during construction
   - Improves code readability and maintainability

3. **Strategy Pattern**
   - Implemented in authentication mechanism
   - Allows switching between different authentication strategies
   - Facilitates future extensions of authentication methods

4. **DTO Pattern**
   - Separates domain models from API response/request objects
   - Provides data validation and transformation layer
   - Reduces unnecessary data exposure

5. **Factory Pattern**
   - Used for creating complex objects and service implementations
   - Centralizes object creation logic
   - Promotes loose coupling

## Technical Stack

- Java 11
- Spring Boot 2.7.x
- Spring Security with JWT
- Spring Batch
- Spring Data JPA
- H2 Database
- Maven
- Lombok
- Swagger/OpenAPI

## Setup & Installation

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Git

### Getting Started

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd Account-Record-Service
   ```

2. Configure application properties (optional):
   - Update `src/main/resources/application.properties` if needed
   - Default configuration uses H2 in-memory database

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the application:
   - Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
   - API Documentation: http://localhost:8080/swagger-ui.html

## API Documentation

### Authentication

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "user",
    "password": "password",
    "email": "user@example.com"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "user",
    "password": "password"
}
```

### Account Operations

#### Get All Accounts (Paginated)
```http
GET /api/accounts?page=0&size=10
Authorization: Bearer {jwt-token}
```

#### Get Account by Number
```http
GET /api/accounts/{accountNumber}
Authorization: Bearer {jwt-token}
```

#### Get Accounts by Customer ID
```http
GET /api/accounts/by-customer/{customerId}?page=0&size=10
Authorization: Bearer {jwt-token}
```

#### Search Accounts by Description
```http
GET /api/accounts/by-description?description={searchTerm}&page=0&size=10
Authorization: Bearer {jwt-token}
```

#### Update Account
```http
PUT /api/accounts/{accountNumber}
Authorization: Bearer {jwt-token}
Content-Type: application/json

{
    "description": "Updated description"
}
```

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control
- Optimistic locking for concurrent updates

## Error Handling

The application implements a global exception handling mechanism that returns appropriate HTTP status codes and error messages:

- 400 Bad Request - Invalid input
- 401 Unauthorized - Invalid credentials
- 403 Forbidden - Insufficient permissions
- 404 Not Found - Resource not found
- 409 Conflict - Concurrent modification
- 500 Internal Server Error - Server-side errors

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.