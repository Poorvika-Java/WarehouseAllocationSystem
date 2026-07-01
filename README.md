# Warehouse Allocation System

A Spring Boot REST API project for managing warehouses, products, inventories, stock allocation, allocation history, and stock transfers.

The system allows organizations to maintain inventory across multiple warehouses while efficiently allocating products based on available stock. It follows a layered architecture with proper exception handling, validation, pagination, Swagger documentation, and unit testing.

---

# Project Features

## Warehouse Management

- Create Warehouse
- Get Warehouse by Id
- Get All Warehouses
- Update Warehouse
- Delete Warehouse
- Pagination and Sorting

---

## Product Management

- Create Product
- Get Product by Id
- Get All Products
- Update Product
- Delete Product
- Pagination and Sorting

---

## Warehouse Inventory

- Add Inventory
- Update Inventory
- Get Inventory by Id
- Get All Inventory
- Get Inventory By Warehouse
- Get Inventory By Product
- Delete Inventory

---

## Stock Allocation

- Allocate Product
- Allocation based on available warehouse stock
- Reduce inventory automatically
- Reduce product total stock automatically
- Save allocation history
- Search allocation by
  - Product
  - Warehouse
  - Date Range

---

## Allocation History

- Get Allocation History
- Search History by Product
- Search History by Warehouse
- Pagination Support

---

## Stock Transfer

- Transfer stock between warehouses
- Validate source stock
- Update inventory automatically
- Store transfer details
- Get transfer by Id
- Get all transfers

---

# Technologies Used

- Java 21
- Spring Boot 3.5.x
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Lombok
- Swagger OpenAPI
- JUnit 5
- Mockito
- Postman

---

# Project Structure

src
├── controller
├── service
├── serviceimpl
├── repository
├── entity
├── mapper
├── requestdto
├── responsedto
├── exception
├── enums
├── constants
└── config

---

# Architecture

The project follows Layered Architecture.

Controller

↓

Service

↓

Repository

↓

Database

---

# Database

Database : MySQL

Schema is automatically created using schema.sql.

No sample data is preloaded.

---

# API Documentation

Swagger UI

http://localhost:8080/swagger-ui/index.html

OpenAPI JSON

http://localhost:8080/v3/api-docs

---

# Exception Handling

Global Exception Handler is implemented.

Custom Exceptions include

- ProductNotFoundException
- WarehouseNotFoundException
- InventoryNotFoundException
- AllocationNotFoundException
- AllocationHistoryNotFoundException
- StockTransferNotFoundException
- DuplicateResourceException
- InvalidRequestException
- WarehouseCapacityExceededException
- InsufficientStockException

---

# Validation

Input validation is implemented using Jakarta Validation.

Examples

- Required fields
- Positive quantity validation
- Warehouse status validation
- Duplicate inventory validation
- Warehouse capacity validation

---

# Pagination and Sorting

Implemented for

- Warehouses
- Products
- Inventory
- Allocations
- Allocation History
- Stock Transfers

---

# Logging

Application logging is implemented using SLF4J.

Logs include

- Create operations
- Update operations
- Delete operations
- Allocation
- Stock Transfer
- Errors

---

# Testing

Unit testing is implemented using

- JUnit 5
- Mockito

Service layer test cases are included for

- Warehouse
- Product
- Warehouse Inventory
- Allocation
- Allocation History
- Stock Transfer

---

# API Testing

API testing has been performed using Postman.

The Postman Collection is included in this repository.

---

# Output Screenshots

The repository contains screenshots for

- Swagger UI
- Project Structure
- API Outputs
- Pagination
- Validation
- Unit Test Results

---

# Non Functional Features

- Layered Architecture
- Global Exception Handling
- Logging
- Validation
- Pagination
- Sorting
- Swagger Documentation
- Unit Testing
- Transaction Management

---

# Future Enhancements

- JWT Authentication
- Role Based Authorization
- Docker Support
- CI/CD Pipeline
- Redis Caching
- Email Notifications
- Dashboard
- Performance Testing using Apache JMeter

---

# Author

Poorvika HR
