# Assumptions and Design Decisions

## Assumptions

- Warehouses must exist before inventory can be added.
- Products must exist before inventory can be created.
- Warehouse status should be ACTIVE before allocation.
- Every warehouse maintains its own inventory.
- Product stock is allocated only if sufficient quantity is available.
- Allocation is performed from a warehouse having sufficient stock.
- Inventory quantity is updated immediately after allocation.
- Allocation history is maintained for every successful allocation.
- Soft delete is used for Warehouse and Product entities.
- Database schema is created automatically using schema.sql.
- Sample data is intentionally not included.
- APIs are tested using Postman.
- Swagger is used for API documentation.

---

## Design Decisions

### Layered Architecture

Controller → Service → Repository → Database

---

### DTO Pattern

Separate Request DTOs and Response DTOs are used.

---

### Repository Pattern

Spring Data JPA repositories handle database operations.

---

### Mapping Layer

Dedicated Mapper classes convert Entity objects to DTOs.

---

### Global Exception Handling

Centralized exception handling is implemented using @RestControllerAdvice.

---

### Transaction Management

Transactional methods ensure database consistency during allocation and stock transfer operations.

---

### Pagination

Pagination is implemented for listing APIs.

---

### Sorting

Sorting support is provided where applicable.

---

### Validation

Jakarta Validation is used for validating request data.

---

### Logging

SLF4J logging is implemented for important business operations and error tracking.

---

### Swagger Documentation

Springdoc OpenAPI is used to generate interactive API documentation.

---

### Unit Testing

JUnit 5 and Mockito are used for service layer unit testing.

---

## Author

Poorvika HR
