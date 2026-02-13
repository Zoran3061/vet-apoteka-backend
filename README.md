#  Vet Apoteka – Backend

Spring Boot REST API for Veterinary Pharmacy system.

##  Technologies

- Java 17
- Spring Boot
- Spring Security (JWT Authentication)
- MySQL
- JPA / Hibernate
- Maven

##  Roles

- ADMIN
- USER
- MAGACIONER

##  Features

- JWT authentication & authorization
- Role-based access control
- Product management (CRUD)
- User management (Admin only)
- Order management
- Warehouse order status update

##  Database

MySQL database name:
  - vet_apoteka.sql

##  Update `application.properties`:
  - spring.datasource.url=jdbc:mysql://localhost:3306/vet_apoteka
  - spring.datasource.username=root
  - spring.datasource.password=
    
##  Run project
  - mvn clean install
  - mvn spring-boot:run
    
## Server runs on:
  - http://localhost:8080

## 📡 API Base URL
  - http://localhost:8080/api

---

#  Author

Zoran Višić  
Metropolitan University 
