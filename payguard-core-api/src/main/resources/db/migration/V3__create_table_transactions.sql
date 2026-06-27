CREATE TABLE tb_transactions (
                                 id BINARY(16) NOT NULL,
                                 account_origin_id BINARY(16) NOT NULL,
                                 account_destination_id BINARY(16) NOT NULL,
                                 amount DECIMAL(19,4) NOT NULL,
                                 currency VARCHAR(3) NOT NULL,
                                 status VARCHAR(20) NOT NULL,
                                 created_at DATETIME NOT NULL,
                                 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;