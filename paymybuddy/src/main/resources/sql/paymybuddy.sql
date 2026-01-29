-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : jeu. 29 jan. 2026 à 19:44
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `paymybuddy`
--

-- --------------------------------------------------------

--
-- Structure de la table `transactions`
--

CREATE TABLE `transactions` (
  `id` bigint(20) NOT NULL,
  `sender_id` bigint(20) NOT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` decimal(12,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `transactions`
--

INSERT INTO `transactions` (`id`, `sender_id`, `receiver_id`, `description`, `amount`, `created_at`) VALUES
(1, 1, 2, 'Remboursement déjeuner', 12.50, '2026-01-29 18:32:12'),
(2, 2, 1, 'Cinéma', 9.00, '2026-01-29 18:32:12'),
(3, 1, 3, 'Café', 3.20, '2026-01-29 18:32:12'),
(4, 3, 4, 'Billet de train', 25.00, '2026-01-29 18:32:12'),
(5, 4, 5, 'Sortie concert', 45.00, '2026-01-29 18:32:12'),
(6, 5, 2, 'Courses partagées', 18.75, '2026-01-29 18:32:12'),
(7, 6, 1, 'Anniversaire', 30.00, '2026-01-29 18:32:12'),
(8, 7, 4, 'Taxi', 14.90, '2026-01-29 18:32:12'),
(9, 8, 5, 'Déjeuner pro', 22.00, '2026-01-29 18:32:12'),
(10, 9, 6, 'Week-end', 50.00, '2026-01-29 18:32:12');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password_hash`, `created_at`) VALUES
(1, 'alice', 'alice@example.com', '$2a$10$hashalice', '2026-01-29 18:31:33'),
(2, 'bob', 'bob@example.com', '$2a$10$hashbob', '2026-01-29 18:31:33'),
(3, 'carol', 'carol@example.com', '$2a$10$hashcarol', '2026-01-29 18:31:33'),
(4, 'david', 'david@example.com', '$2a$10$hashdavid', '2026-01-29 18:31:33'),
(5, 'emma', 'emma@example.com', '$2a$10$hashemma', '2026-01-29 18:31:33'),
(6, 'frank', 'frank@example.com', '$2a$10$hashfrank', '2026-01-29 18:31:33'),
(7, 'grace', 'grace@example.com', '$2a$10$hashgrace', '2026-01-29 18:31:33'),
(8, 'henry', 'henry@example.com', '$2a$10$hashhenry', '2026-01-29 18:31:33'),
(9, 'irene', 'irene@example.com', '$2a$10$hashirene', '2026-01-29 18:31:33'),
(10, 'jack', 'jack@example.com', '$2a$10$hashjack', '2026-01-29 18:31:33');

-- --------------------------------------------------------

--
-- Structure de la table `user_connections`
--

CREATE TABLE `user_connections` (
  `user_id` bigint(20) NOT NULL,
  `connection_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user_connections`
--

INSERT INTO `user_connections` (`user_id`, `connection_id`) VALUES
(1, 2),
(1, 3),
(1, 4),
(2, 3),
(2, 5),
(3, 6),
(4, 5),
(4, 7),
(5, 8),
(6, 9);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_tx_sender_created_at` (`sender_id`,`created_at`),
  ADD KEY `idx_tx_receiver_created_at` (`receiver_id`,`created_at`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_users_username` (`username`),
  ADD UNIQUE KEY `uq_users_email` (`email`);

--
-- Index pour la table `user_connections`
--
ALTER TABLE `user_connections`
  ADD PRIMARY KEY (`user_id`,`connection_id`),
  ADD KEY `idx_uc_connection_id` (`connection_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `fk_tx_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_tx_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE;

--
-- Contraintes pour la table `user_connections`
--
ALTER TABLE `user_connections`
  ADD CONSTRAINT `fk_uc_connection` FOREIGN KEY (`connection_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_uc_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
