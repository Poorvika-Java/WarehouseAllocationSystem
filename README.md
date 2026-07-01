# Warehouse Allocation System

## Project Overview

The Warehouse Allocation System is a Spring Boot RESTful API application developed to manage warehouses, products, inventories, stock allocations, stock transfers, and allocation history. The application provides a centralized solution for maintaining warehouse operations while ensuring inventory consistency, stock availability, and efficient allocation management.

The project follows a layered architecture using Spring Boot best practices with DTOs, Service Layer, Repository Layer, Global Exception Handling, Pagination, Validation, Logging, Swagger documentation, and Unit Testing.

---

# Objectives

The main objective of this project is to:

- Manage multiple warehouses
- Maintain product information
- Track warehouse inventory
- Allocate products automatically
- Transfer stock between warehouses
- Maintain allocation history
- Provide REST APIs with proper validation
- Handle exceptions globally
- Support pagination and sorting
- Provide API documentation using Swagger
- Ensure business logic through unit testing

---

# Technologies Used

| Technology | Version |
|------------|----------|
| Java | 21 |
| Spring Boot | 3.5.3 |
| Spring Data JPA | Latest |
| Hibernate | Latest |
| Maven | Latest |
| MySQL | 8.x |
| Lombok | Latest |
| Swagger OpenAPI | SpringDoc |
| JUnit 5 | Latest |
| Mockito | Latest |

---

# Project Architecture

The application follows a layered architecture.

```

Controller

↓

Service

↓

Repository

↓

Database

```

Additional Layers

- DTO Layer
- Mapper Layer
- Exception Layer
- Entity Layer
- Configuration Layer

---

# Project Structure

```

WarehouseAllocationSystem

│

├── controller

├── service

├── serviceimpl

├── repository

├── entity

├── mapper

├── requestdto

├── responsedto

├── enums

├── exception

├── config

├── resources

├── test

└── pom.xml

```

---

# Modules Implemented

## Warehouse Module

Features

- Create Warehouse
- Update Warehouse
- Get Warehouse By Id
- Get All Warehouses
- Activate Warehouse
- Deactivate Warehouse
- Soft Delete Warehouse

---

## Product Module

Features

- Create Product
- Update Product
- Get Product
- Get All Products
- Delete Product

---

## Warehouse Inventory Module

Features

- Add Inventory
- Update Inventory
- Delete Inventory
- Get Inventory
- Get Inventory By Warehouse
- Get Inventory By Product
- Warehouse Capacity Validation
- Warehouse Status Validation

---

## Allocation Module

Features

- Allocate Product
- Automatic Warehouse Selection
- Get Allocation
- Get Allocation By Product
- Get Allocation By Warehouse
- Get Allocation Between Dates

---

## Allocation History Module

Features

- Get History By Id
- Get All History
- Get History By Product
- Get History By Warehouse

---

## Stock Transfer Module

Features

- Transfer Stock
- Get Transfer By Id
- Get All Transfers

---

# Validation Implemented

The project validates:

- Duplicate Warehouse
- Duplicate Product
- Warehouse Capacity
- Warehouse Status
- Product Availability
- Inventory Availability
- Quantity Validation
- Null Quantity Validation
- Negative Quantity Validation
- Resource Existence Validation

---

# Exception Handling

Implemented using GlobalExceptionHandler.

Exceptions include:

- WarehouseNotFoundException
- ProductNotFoundException
- InventoryNotFoundException
- AllocationNotFoundException
- AllocationHistoryNotFoundException
- StockTransferNotFoundException
- DuplicateResourceException
- WarehouseCapacityExceededException
- InsufficientStockException
- InvalidRequestException

---

# Pagination & Sorting

Implemented in all listing APIs.

Supports:

- page
- size
- sortBy
- direction

Example

```

?page=0&size=10&sortBy=id&direction=ASC

```

---

# API Documentation

Swagger UI is integrated.

```

http://localhost:8080/swagger-ui/index.html

```

---

# Testing

The application has been tested using:

- Postman
- Swagger UI
- JUnit 5
- Mockito
- MySQL Workbench

---

# Unit Testing

Service layer test cases implemented for:

- WarehouseServiceImpl
- ProductServiceImpl
- WarehouseInventoryServiceImpl
- AllocationServiceImpl
- AllocationHistoryServiceImpl
- StockTransferServiceImpl

---

# Logging

SLF4J Logging has been implemented to monitor:

- API execution
- Successful operations
- Validation failures
- Exception handling

---

# Security

Authentication and authorization are not implemented in this project as the focus is on warehouse management business logic.

---

# Future Enhancements

- JWT Authentication
- Role Based Authorization
- Dashboard
- Email Notification
- Docker Deployment
- Redis Cache
- Kafka Integration

---

# Author

**Poorvika HR**