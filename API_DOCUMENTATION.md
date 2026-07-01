# Warehouse Allocation System API Documentation

---

# Base URL

```
http://localhost:8080
```

---

# Swagger

```
http://localhost:8080/swagger-ui/index.html
```

---

# Modules

1. Warehouse
2. Product
3. Warehouse Inventory
4. Allocation
5. Allocation History
6. Stock Transfer

---

# Warehouse APIs

| Method | Endpoint |
|----------|--------------------------------|
| POST | /api/warehouses |
| PUT | /api/warehouses/{id} |
| GET | /api/warehouses/{id} |
| GET | /api/warehouses |
| PATCH | /api/warehouses/{id}/activate |
| PATCH | /api/warehouses/{id}/deactivate |
| DELETE | /api/warehouses/{id} |

Supports:

- Pagination
- Sorting

---

# Product APIs

| Method | Endpoint |
|----------|--------------------------|
| POST | /api/products |
| PUT | /api/products/{id} |
| GET | /api/products/{id} |
| GET | /api/products |
| DELETE | /api/products/{id} |

Supports

- Pagination
- Sorting

---

# Warehouse Inventory APIs

| Method | Endpoint |
|----------|---------------------------------------------|
| POST | /api/inventories |
| PUT | /api/inventories/{id} |
| GET | /api/inventories/{id} |
| GET | /api/inventories |
| GET | /api/inventories/warehouse/{warehouseId} |
| GET | /api/inventories/product/{productId} |
| DELETE | /api/inventories/{id} |

Supports

- Pagination
- Sorting

---

# Allocation APIs

| Method | Endpoint |
|----------|--------------------------------|
| POST | /api/allocations |
| GET | /api/allocations/{id} |
| GET | /api/allocations |
| GET | /api/allocations/product/{productId} |
| GET | /api/allocations/warehouse/{warehouseId} |
| GET | /api/allocations/date-range |

Supports

- Pagination
- Sorting

---

# Allocation History APIs

| Method | Endpoint |
|----------|----------------------------------------|
| GET | /api/allocation-history/{id} |
| GET | /api/allocation-history |
| GET | /api/allocation-history/product/{productId} |
| GET | /api/allocation-history/warehouse/{warehouseId} |

Supports

- Pagination
- Sorting

---

# Stock Transfer APIs

| Method | Endpoint |
|----------|--------------------------------|
| POST | /api/stock-transfers |
| GET | /api/stock-transfers/{id} |
| GET | /api/stock-transfers |

Supports

- Pagination
- Sorting

---

# Common Query Parameters

```
?page=0
&size=10
&sortBy=id
&direction=ASC
```

---

# Standard Response

```json
{
  "success": true,
  "message": "Operation Successful",
  "data": {},
  "timestamp": "2026-06-30T12:30:15"
}
```

---

# Validation Rules

- Warehouse name must be unique.
- Product SKU must be unique.
- Quantity cannot be null.
- Quantity cannot be negative.
- Warehouse must be ACTIVE.
- Warehouse capacity cannot be exceeded.
- Inventory must exist before update.
- Product must exist.
- Warehouse must exist.
- Stock allocation requires sufficient inventory.

---

# Error Responses

| Status | Description |
|---------|-------------|
| 400 | Bad Request |
| 404 | Resource Not Found |
| 409 | Duplicate Resource |
| 500 | Internal Server Error |

---

# Exception Handling

Implemented using GlobalExceptionHandler.

Exceptions:

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

# Design Decisions

- RESTful API Design
- Layered Architecture
- DTO-Based Communication
- Mapper Classes
- Repository Pattern
- Global Exception Handling
- Transaction Management
- Pagination & Sorting
- Soft Delete Support
- Logging with SLF4J

---

# Assumptions

- Warehouse names are unique.
- Product SKU is unique.
- Allocation is performed only when stock is available.
- Warehouse capacity is validated before inventory creation.
- Soft delete is applied where required.
- Pagination is enabled for list APIs.
- Swagger is available for API testing.
- Unit tests cover service-layer business logic.

---

# Testing

Completed using:

- Postman
- Swagger UI
- JUnit 5
- Mockito
- MySQL

---

# Output Evidence

Project includes screenshots of:

- Swagger UI
- API Testing (Postman)
- Database Records
- Unit Test Results
- JMeter Summary Report (if executed)

---

# Future Enhancements

- JWT Authentication
- Role-Based Access Control
- Docker
- Kubernetes
- Redis
- Kafka
- Email Notification
- Audit Logging

---

# Author

**Poorvika HR**