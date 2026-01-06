
# Hospital Visits

## Project Overview

**Hospital Visits** is a comprehensive hospital management system developed using **Spring Boot**.  
The project was designed as an academic final exam application and demonstrates the practical application of:

- RESTful architecture
- Secure authentication and authorization with JWT
- Role-based access control
- Relational database design
- MVC pattern with Thymeleaf
- Clean separation of concerns

The system supports both **API-based interaction** (for external clients and testing tools such as Postman) and a **server-rendered web interface** for direct user interaction.

---

## Objectives

The main objectives of the project are:

- Simulate the real-world management of a hospital
- Apply enterprise-level backend development practices
- Implement secure authentication mechanisms
- Manage relational data with JPA/Hibernate
- Provide a clear and maintainable architecture
- Ensure full CRUD coverage for core entities

---

## Functional Scope

The application allows authorized users to:

- Register and authenticate
- Manage hospital departments and specializations
- Manage doctors and rooms
- Schedule and reserve medical visits
- View and cancel reservations
- Manage user profiles and credentials

All functionalities are protected according to the user role.

---

## Technology Stack

### Backend
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security

### Security
- JWT (JSON Web Token)
- BCrypt password encoding
- Method-level authorization (`@PreAuthorize`)
- Stateless authentication for REST APIs
- Session-based authentication for web interface

### Frontend
- HTML5
- CSS3
- Thymeleaf

### Database
- PostgreSQL
- pgAdmin4

### Build & Tooling
- Maven
- Postman

---

## Dependencies

The project was initialized using **Spring Initializr** with the following dependencies:

- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- PostgreSQL Driver
- Spring Boot DevTools

---

## Database Design

The database follows a **relational model** designed to reflect real hospital structures.

### Main Entities

- User
- Role
- Department
- Specialization
- Doctor
- Room
- Visit
- Reservation

### Relationships

- A Doctor belongs to one Department and one Specialization
- A Visit is linked to a Doctor, Room, and Patient
- A Reservation associates a Patient with a Visit
- Roles define access permissions

### Resources

- ER Diagram:
  - `database_hospital.pgerd.png`
- SQL Initialization Script:
  - `HospitalDatabase.sql`

---

## Application Architecture

The project follows a **layered architecture**:

- Controller Layer
- Service Layer
- Repository Layer
- Security Layer
- Presentation Layer (Thymeleaf)

### Operating Modes

#### REST API
```
/api/**
```
- Stateless
- JWT secured
- Designed for Postman and external systems

#### Web Interface
```
/**
```
- Session-based
- Form login
- Thymeleaf-rendered views

---

## Security Architecture

Security is implemented using **Spring Security** and includes:

- Custom `UserDetailsService`
- JWT authentication filter
- Separate security chains for API and Web
- Password encryption with BCrypt
- Role-based endpoint protection

### Roles

- ADMIN
- DOCTOR
- PATIENT

Each role has restricted access to specific resources.

---

## User Management

### Authentication
- Registration
- Login (API & Web)

### Profile Management
- View profile
- Update personal information
- Upload and update profile picture
- Change password with database persistence

### Visit Management
- View available visits
- Reserve visits
- View reservations
- Cancel reservations

---

## Entity Management

### Department
- Full CRUD
- Search by name

### Specialization
- Full CRUD

### Room
- Full CRUD
- Search by address

### Doctor
- Full CRUD
- Linked to Department and Specialization
- Unique fiscal code constraint

### Visit
- Scheduling
- Reservation logic
- Cancellation handling

---

## API Testing

All REST endpoints were tested using **Postman**.

Included files:
- `Local Hospital.postman_environment.json`
- `Hospital Visits - Final Exam.postman_collection.json`

---

## Installation Guide

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL
- pgAdmin4
- IDE (IntelliJ IDEA recommended)

### Setup Steps

1. Clone the repository:
```bash
git clone https://github.com/hospital-visits.git
```

2. Create the database using:
```sql
HospitalDatabase.sql
```

3. Configure database credentials in:
```
root/example.env  -> with credentials of your pgAdmin application 
```

4. Build the project:
```bash
mvn clean install
```

---

## Running the Application
Run the application across the main file C:\Users\santo\Dropbox\PC\Downloads\Hospital-Visits-main\Hospital-Visits-main\hospital-visits\src\main\java\com\hospital\hospital_visit\HospitalVisitsApplication.java

### Access URLs

- Web Interface:
  - http://localhost:{port}       ---> port of the application: 8082
- REST API:
  - http://localhost:{port}/api   ---> port of the application: 8082

---

## Troubleshooting

### Authentication Errors
- Ensure JWT token is valid
- Check Authorization header format

### Database Errors
- Verify PostgreSQL service is running
- Check credentials and schema

### Template Errors
- Verify controller mappings
- Ensure template names match HTML files

---

## Project Structure

```
src/main/java/com/hospital/hospital_visits/
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── security
src/main/resources/
 ├── templates
 ├── static
 └── application.properties
```

---

## Environment Variables
This project **does not store secrers in the repository**.
Configure secrets using environment variables.

### 1) Create your local `.env`
1. Copy `example.env` to `.env`
2. Replace placeholders with local values

> The `.env` file is listed in `.gitignore` and must not be commited

### 2) Variables used
- `DB_URL`=jdbc=postgresql://localhost:5432/hospital_visits_db
- `DB_USERNAME`
- `DB_PASSWORD`

# token JWT:

- `JWT_SECRET` (a long and randomic secret)
- `JWT_EXPIRATION` (milliseconds, `360000` for 1 hour)

Spring Boot reads them from the environment and maps them in `application.properties`.

## Run Locally (IntelliJ)
1. Open **Run -> Edit Configurations...**
2. Add the environment variables
3. Run the apllication

## Author

**Prestandrea Santo**  
Informatics Student – University of Epicode  
Academic Project - s00007624

---
