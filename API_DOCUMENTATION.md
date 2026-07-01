# Warehouse Allocation System API Documentation

## Base URL

http://localhost:8080

---

## Swagger UI

http://localhost:8080/swagger-ui/index.html

---

## OpenAPI JSON

http://localhost:8080/v3/api-docs

---

## Authentication

Authentication is not implemented.

All APIs are publicly accessible.

---

## Modules

- Warehouse
- Product
- Warehouse Inventory
- Allocation
- Allocation History
- Stock Transfer

---

## Warehouse APIs

POST /api/warehouses

GET /api/warehouses/{id}

GET /api/warehouses

PUT /api/warehouses/{id}

DELETE /api/warehouses/{id}

---

## Product APIs

POST /api/products

GET /api/products/{id}

GET /api/products

PUT /api/products/{id}

DELETE /api/products/{id}

---

## Warehouse Inventory APIs

POST /api/inventory

PUT /api/inventory/{id}

GET /api/inventory/{id}

GET /api/inventory

GET /api/inventory/warehouse/{warehouseId}

GET /api/inventory/product/{productId}

DELETE /api/inventory/{id}

---

## Allocation APIs

POST /api/allocations

GET /api/allocations/{id}

GET /api/allocations

GET /api/allocations/product/{productId}

GET /api/allocations/warehouse/{warehouseId}

GET /api/allocations/date-range

---

## Allocation History APIs

GET /api/allocation-history/{id}

GET /api/allocation-history

GET /api/allocation-history/product/{productId}

GET /api/allocation-history/warehouse/{warehouseId}

---

## Stock Transfer APIs

POST /api/stock-transfers

GET /api/stock-transfers/{id}

GET /api/stock-transfers

---

## Features

- CRUD Operations
- Pagination
- Sorting
- Validation
- Exception Handling
- DTO Pattern
- Transaction Management
- Swagger Documentation
- Soft Delete
- Logging

---

## HTTP Status Codes

200 OK

201 CREATED

400 BAD REQUEST

404 NOT FOUND

409 CONFLICT

500 INTERNAL SERVER ERROR

---

## Author

Poorvika HR
