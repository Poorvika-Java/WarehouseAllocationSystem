# Assumptions and Design Decisions

This document describes the assumptions made during the development of the Warehouse Allocation System and the major design decisions followed throughout the project.

---

# Assumptions

The following assumptions were considered while implementing the project.

## Warehouse

- Warehouse names are unique throughout the system.
- A warehouse must be in ACTIVE status before inventory operations can be performed.
- Soft deleted warehouses are not returned in API responses.
- Warehouse capacity cannot be exceeded while adding or updating inventory.

---

## Product

- Product SKU is unique.
- Total stock is always maintained correctly.
- Deleted products are not available for allocation or inventory operations.

---

## Warehouse Inventory

- A warehouse can have only one inventory record for a particular product.
- Inventory quantity cannot be null.
- Inventory quantity cannot be negative.
- Inventory operations require valid warehouse and product records.
- Inventory quantity should always remain within warehouse capacity.

---

## Allocation

- Products can only be allocated when sufficient stock is available.
- Allocation automatically reduces warehouse inventory.
- Product total stock is updated after successful allocation.
- Every successful allocation creates an allocation history record.
- The warehouse selected for allocation must have sufficient inventory.

---

## Allocation History

- Allocation history is generated automatically after every successful allocation.
- History records are maintained only for completed allocation operations.

---

## Stock Transfer

- Source and destination warehouses must exist.
- Source and destination warehouses cannot be the same.
- Stock transfer quantity must be greater than zero.
- Stock transfer updates inventory in both warehouses.
- Transfer status is maintained for every transfer.

---

## General Assumptions

- Pagination is supported for all listing APIs.
- Sorting is available wherever applicable.
- Validation errors return HTTP 400.
- Resource not found errors return HTTP 404.
- Duplicate resource errors return HTTP 409.
- All API responses follow a common response structure.
- Business validations are handled inside the service layer.

---

# Design Decisions

The project follows standard Spring Boot development practices for better maintainability and scalability.

---

## Layered Architecture

The application follows a layered architecture consisting of:

- Controller Layer
- Service Layer
- Repository Layer
- Database Layer

This improves code organization and separation of responsibilities.

---

## DTO Pattern

Separate Request DTOs and Response DTOs are used instead of exposing entity classes directly.

Benefits:

- Better security
- Cleaner API responses
- Easier maintenance

---

## Mapper Classes

Dedicated mapper classes are used to convert entities into DTOs and vice versa.

Benefits:

- Reusable conversion logic
- Cleaner service implementation
- Reduced code duplication

---

## Repository Pattern

Spring Data JPA repositories handle all database operations.

Benefits:

- Simplified CRUD operations
- Less boilerplate code
- Better maintainability

---

## Global Exception Handling

A centralized GlobalExceptionHandler handles all application exceptions.

Benefits:

- Consistent error responses
- Cleaner controller code
- Easier debugging

---

## Validation

Validation is implemented using Bean Validation annotations along with custom business validations inside the service layer.

Examples include:

- Duplicate resource validation
- Warehouse status validation
- Capacity validation
- Stock availability validation
- Quantity validation

---

## Transaction Management

Service classes use @Transactional to maintain data consistency during database operations.

---

## Pagination and Sorting

Pagination and sorting are implemented for all list APIs to improve performance and reduce response size.

---

## Logging

SLF4J logging is implemented throughout the service layer to record:

- API requests
- Successful operations
- Validation failures
- Exception details

---

## Swagger Integration

Swagger OpenAPI is integrated for API documentation and testing.

Benefits:

- Interactive API documentation
- Easy endpoint testing
- Better developer experience

---

## Unit Testing

JUnit 5 and Mockito are used for testing service layer business logic.

Mock objects are used to isolate dependencies and verify application behavior.

---

## Soft Delete

Soft delete is implemented where applicable to preserve historical data instead of permanently removing records.

---

## REST API Design

RESTful principles followed include:

- Resource-based URLs
- Proper HTTP methods
- Standard HTTP status codes
- Consistent API response format

---

## Database Design

The database is normalized with relationships among:

- Warehouse
- Product
- Warehouse Inventory
- Allocation
- Allocation History
- Stock Transfer

Foreign key constraints ensure referential integrity and consistency.

---

# Author

**Poorvika HR**