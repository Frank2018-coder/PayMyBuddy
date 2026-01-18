-- Pay My Buddy - schema.sql (MySQL compatible, sans CHECK)
-- Prototype V1 : users, connections, transactions
-- Règles métier (auto-connexion, montants positifs) gérées côté application Java

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS connections;
DROP TABLE IF EXISTS users;

-- =========================
-- TABLE USERS
-- =========================
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================
-- TABLE CONNECTIONS
-- =========================
CREATE TABLE connections (
  user_id BIGINT NOT NULL,
  buddy_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, buddy_id),
  CONSTRAINT fk_connections_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_connections_buddy FOREIGN KEY (buddy_id) REFERENCES users(id)
);

-- =========================
-- TABLE TRANSACTIONS
-- =========================
CREATE TABLE transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  fee DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  description VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_transactions_sender FOREIGN KEY (sender_id) REFERENCES users(id),
  CONSTRAINT fk_transactions_receiver FOREIGN KEY (receiver_id) REFERENCES users(id)
);

CREATE INDEX idx_transactions_sender ON transactions(sender_id);
CREATE INDEX idx_transactions_receiver ON transactions(receiver_id);

-- =========================
-- DONNÉES DE DÉMONSTRATION
-- =========================
-- NOTE : password_hash = valeurs factices pour le prototype

INSERT INTO users (username, email, password_hash, balance) VALUES
('Laura', 'laura@paymybuddy.com', '$2a$10$demoHashLaura..............................', 250.00),
('Anne',  'anne@paymybuddy.com',  '$2a$10$demoHashAnne...............................', 120.00),
('Clara', 'clara@paymybuddy.com', '$2a$10$demoHashClara..............................', 80.00),
('Luc',   'luc@paymybuddy.com',   '$2a$10$demoHashLuc................................', 60.00),
('Admin', 'admin@paymybuddy.com', '$2a$10$demoHashAdmin..............................', 500.00);

-- Relations (connections)
INSERT INTO connections (user_id, buddy_id) VALUES
(1, 2), -- Laura -> Anne
(1, 3), -- Laura -> Clara
(1, 4), -- Laura -> Luc
(2, 1), -- Anne -> Laura
(3, 1); -- Clara -> Laura

-- Transactions (frais 0,5 %)
INSERT INTO transactions (sender_id, receiver_id, amount, fee, description) VALUES
(1, 2, 10.00, 0.05, 'Restaurant'),
(1, 3, 25.00, 0.13, 'Voyage'),
(1, 4, 8.00, 0.04, 'Billets de cinéma'),
(2, 1, 15.00, 0.08, 'Remboursement');
