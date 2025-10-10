CREATE TABLE orderStatus (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

INSERT INTO orderStatus (id, code, description) VALUES
    (1, 'ORDERED', 'Order placed by account'),
    (2, 'IN_PROGRESS', 'Order is being prepared or shipped'),
    (3, 'ARRIVED', 'Order has arrived to customer'),
    (4, 'CANCELLED', 'Order has been cancelled');

CREATE TABLE item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000),
    price DECIMAL(12,2) NOT NULL,
    stockQuantity INT NOT NULL
);

CREATE TABLE storeOrder (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    accountId BIGINT NOT NULL,
    statusId BIGINT NOT NULL,
    totalAmount DECIMAL(12,2) NOT NULL,
    createdAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP NULL DEFAULT NULL,
    CONSTRAINT fk_storeOrder_account FOREIGN KEY (accountId) REFERENCES account(id),
    CONSTRAINT fk_storeOrder_status FOREIGN KEY (statusId) REFERENCES orderStatus(id)
);

CREATE TABLE orderItem (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    orderId BIGINT NOT NULL,
    itemId BIGINT NOT NULL,
    quantity INT NOT NULL,
    unitPrice DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_orderItem_order FOREIGN KEY (orderId) REFERENCES storeOrder(id) ON DELETE CASCADE,
    CONSTRAINT fk_orderItem_item FOREIGN KEY (itemId) REFERENCES item(id)
);

CREATE INDEX idx_orderItem_orderId ON orderItem(orderId);
CREATE INDEX idx_orderItem_itemId ON orderItem(itemId);
