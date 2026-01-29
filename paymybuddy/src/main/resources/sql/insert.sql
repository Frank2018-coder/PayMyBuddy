USE paymybuddy;

-- Nettoyage (optionnel)
DELETE FROM transactions;
DELETE FROM user_connections;
DELETE FROM users;

-- =========
-- USERS 
-- password_hash : placeholders (à remplacer par BCrypt réels si besoin)
-- =========
INSERT INTO users (username, email, password_hash) VALUES
('alice', 'alice@example.com', '$2a$10$hashalice'),
('bob',   'bob@example.com',   '$2a$10$hashbob'),
('carol', 'carol@example.com', '$2a$10$hashcarol'),
('david', 'david@example.com', '$2a$10$hashdavid'),
('emma',  'emma@example.com',  '$2a$10$hashemma'),
('frank', 'frank@example.com', '$2a$10$hashfrank'),
('grace', 'grace@example.com', '$2a$10$hashgrace'),
('henry', 'henry@example.com', '$2a$10$hashhenry'),
('irene', 'irene@example.com', '$2a$10$hashirene'),
('jack',  'jack@example.com',  '$2a$10$hashjack');

-- =========
-- USER_CONNECTIONS 
-- =========
INSERT INTO user_connections (user_id, connection_id) VALUES
(1, 2),  -- alice - bob
(1, 3),  -- alice - carol
(1, 4),  -- alice - david
(2, 3),  -- bob - carol
(2, 5),  -- bob - emma
(3, 6),  -- carol - frank
(4, 5),  -- david - emma
(4, 7),  -- david - grace
(5, 8),  -- emma - henry
(6, 9);  -- frank - irene

-- =========
-- TRANSACTIONS 
-- =========
INSERT INTO transactions (sender_id, receiver_id, description, amount) VALUES
(1, 2, 'Remboursement déjeuner', 12.50),
(2, 1, 'Cinéma', 9.00),
(1, 3, 'Café', 3.20),
(3, 4, 'Billet de train', 25.00),
(4, 5, 'Sortie concert', 45.00),
(5, 2, 'Courses partagées', 18.75),
(6, 1, 'Anniversaire', 30.00),
(7, 4, 'Taxi', 14.90),
(8, 5, 'Déjeuner pro', 22.00),
(9, 6, 'Week-end', 50.00);
