DROP TABLE IF EXISTS allocation_history;
DROP TABLE IF EXISTS stock_transfer;
DROP TABLE IF EXISTS allocation;
DROP TABLE IF EXISTS warehouse_inventory;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS warehouse;
CREATE TABLE warehouse (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    capacity INT,
    status VARCHAR(50),
    deleted BOOLEAN DEFAULT FALSE,
    deleted_at DATETIME NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100) UNIQUE,
    total_stock INT DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE,
    deleted_at DATETIME NULL
);

CREATE TABLE warehouse_inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT,
    product_id BIGINT,
    available_quantity INT DEFAULT 0,
    version INT,
    CONSTRAINT fk_inventory_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(id),

    CONSTRAINT fk_inventory_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
);


CREATE TABLE allocation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT,
    warehouse_id BIGINT,
    quantity INT NOT NULL,
    allocated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30),
    CONSTRAINT fk_allocation_product
        FOREIGN KEY (product_id)
        REFERENCES product(id),

    CONSTRAINT fk_allocation_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouse(id)
);


CREATE TABLE allocation_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    allocation_id BIGINT,
    status VARCHAR(30),
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_history_allocation
        FOREIGN KEY (allocation_id)
        REFERENCES allocation(id)
);


CREATE TABLE stock_transfer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    source_warehouse_id BIGINT,
    target_warehouse_id BIGINT,
    product_id BIGINT,
    quantity INT NOT NULL,
    transfer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transfer_source
        FOREIGN KEY (source_warehouse_id)
        REFERENCES warehouse(id),

    CONSTRAINT fk_transfer_target
        FOREIGN KEY (target_warehouse_id)
        REFERENCES warehouse(id),

    CONSTRAINT fk_transfer_product
        FOREIGN KEY (product_id)
        REFERENCES product(id)
);


CREATE INDEX idx_inventory_warehouse ON warehouse_inventory(warehouse_id);
CREATE INDEX idx_inventory_product ON warehouse_inventory(product_id);

CREATE INDEX idx_allocation_product ON allocation(product_id);
CREATE INDEX idx_allocation_warehouse ON allocation(warehouse_id);
CREATE INDEX idx_allocation_date ON allocation(allocated_at);

CREATE INDEX idx_transfer_source ON stock_transfer(source_warehouse_id);
CREATE INDEX idx_transfer_target ON stock_transfer(target_warehouse_id);