# Help Desk Enterprise
REST API for a Help Desk system built with Java and Spring Boot.

## About

Help Desk Enterprise is a REST API designed to manage support tickets.

The application allows:

- User authentication
- Ticket management
- Ticket assignment
- Ticket status updates
- Public and internal comments
- Pagination
- Role-based authorization

## Technologies

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- MySQL
- Hibernate
- MapStruct
- Lombok
- Swagger/OpenAPI
- JUnit 5
- Mockito
- Maven

## Architecture

The project follows a layered architecture:

## Architecture

```text
Controller
     │
     ▼
Service
     │
     ▼
Repository
     │
     ▼
Database
```

The application also uses:

- DTOs
- MapStruct
- Global Exception Handler
- JWT Authentication

## Features

### Authentication

- Register
- Login with JWT

### Users

- List users
- Change role
- Change status

### Tickets

- Create
- Update
- Assign
- Close
- List
- Filter

### Comments

- Create
- Update
- Delete
- List comments

## Tests

The project contains:

- Service Tests
- Repository Tests
- Controller Tests
- JWT Tests

Built using:

- JUnit 5
- Mockito
- MockMvc

## Running

Clone the project

git clone https://github.com/GabrielMontini30/Help_Desk_Enterprise

cd help-desk-enterprise

mvn spring-boot:run

## Database

MySQL

Configure:

application.yml

spring:
datasource:
url:
username:
password:

## API Documentation
http://localhost:8080/swagger-ui/index.html

## Author

Gabriel Montini

LinkedIn:
www.linkedin.com/in/gabriel-montini

GitHub:
https://github.com/GabrielMontini30
