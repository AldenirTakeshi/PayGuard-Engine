CREATE TABLE accounts (
                          id BINARY(16) NOT NULL,
                          number VARCHAR(20) NOT NULL,
                          holder_name VARCHAR(100) NOT NULL,
                          created_at DATETIME NOT NULL,
                          PRIMARY KEY (id),
                          CONSTRAINT uk_accounts_number UNIQUE (number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE ledger_entries (
                                id BINARY(16) NOT NULL,
                                account_id BINARY(16) NOT NULL,
                                type VARCHAR(6) NOT NULL,
                                amount DECIMAL(19, 4) NOT NULL,
                                description VARCHAR(255),
                                created_at DATETIME NOT NULL,
                                PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;