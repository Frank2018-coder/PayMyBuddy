USE paymybuddy;

-- =========================================
-- USERS
-- =========================================

-- Récupérer un user par email (login)
SELECT id, username, email, password_hash
FROM users
WHERE email = ?;

-- Récupérer un user par username
SELECT id, username, email
FROM users
WHERE username = ?;

-- =========================================
-- CONNECTIONS
-- =========================================

-- Liste des connexions d’un utilisateur (param = userId)
SELECT u2.id, u2.username, u2.email
FROM user_connections uc
JOIN users u2
  ON u2.id = CASE
              WHEN uc.user_id = ? THEN uc.connection_id
              ELSE uc.user_id
            END
WHERE ? IN (uc.user_id, uc.connection_id)
ORDER BY u2.username;

-- Vérifier si deux users sont déjà connectés
SELECT 1
FROM user_connections
WHERE (user_id = ? AND connection_id = ?)
   OR (user_id = ? AND connection_id = ?)
LIMIT 1;

-- Nombre de connexions
SELECT COUNT(*) AS connections_count
FROM user_connections
WHERE ? IN (user_id, connection_id);

-- =========================================
-- TRANSACTIONS / HISTORIQUE
-- =========================================

-- Historique complet (envoyées + reçues)
SELECT
  t.id,
  t.created_at,
  t.description,
  t.amount,
  t.sender_id,
  su.username AS sender_username,
  t.receiver_id,
  ru.username AS receiver_username,
  CASE
    WHEN t.sender_id = ? THEN 'SENT'
    ELSE 'RECEIVED'
  END AS direction
FROM transactions t
JOIN users su ON su.id = t.sender_id
JOIN users ru ON ru.id = t.receiver_id
WHERE t.sender_id = ? OR t.receiver_id = ?
ORDER BY t.created_at DESC, t.id DESC;

-- Historique envoyées
SELECT t.id, t.created_at, t.description, t.amount,
       ru.username AS receiver_username
FROM transactions t
JOIN users ru ON ru.id = t.receiver_id
WHERE t.sender_id = ?
ORDER BY t.created_at DESC, t.id DESC;

-- Historique reçues
SELECT t.id, t.created_at, t.description, t.amount,
       su.username AS sender_username
FROM transactions t
JOIN users su ON su.id = t.sender_id
WHERE t.receiver_id = ?
ORDER BY t.created_at DESC, t.id DESC;

-- Historique paginé (LIMIT/OFFSET)
SELECT
  t.id, t.created_at, t.description, t.amount,
  su.username AS sender_username,
  ru.username AS receiver_username
FROM transactions t
JOIN users su ON su.id = t.sender_id
JOIN users ru ON ru.id = t.receiver_id
WHERE t.sender_id = ? OR t.receiver_id = ?
ORDER BY t.created_at DESC, t.id DESC
LIMIT ? OFFSET ?;

-- =========================================
-- SOLDE (calculé) : net = reçus - envoyés
-- =========================================

-- Solde net d’un utilisateur
SELECT
  u.id,
  u.username,
  COALESCE(SUM(CASE WHEN t.sender_id   = u.id THEN t.amount END), 0) AS total_sent,
  COALESCE(SUM(CASE WHEN t.receiver_id = u.id THEN t.amount END), 0) AS total_received,
  COALESCE(SUM(CASE WHEN t.receiver_id = u.id THEN t.amount ELSE 0 END), 0) -
  COALESCE(SUM(CASE WHEN t.sender_id   = u.id THEN t.amount ELSE 0 END), 0)
  AS net_balance
FROM users u
LEFT JOIN transactions t
  ON t.sender_id = u.id OR t.receiver_id = u.id
WHERE u.id = ?
GROUP BY u.id, u.username;

-- Solde net pour tous les users (classement)
SELECT
  u.id,
  u.username,
  COALESCE(SUM(CASE WHEN t.receiver_id = u.id THEN t.amount ELSE 0 END), 0) -
  COALESCE(SUM(CASE WHEN t.sender_id   = u.id THEN t.amount ELSE 0 END), 0)
  AS net_balance
FROM users u
LEFT JOIN transactions t
  ON t.sender_id = u.id OR t.receiver_id = u.id
GROUP BY u.id, u.username
ORDER BY net_balance DESC;

-- =========================================
-- DESTINATAIRES POUR TRANSFERT (connexions + filtre)
-- =========================================

-- Connexions filtrées par username (typeahead)
SELECT u2.id, u2.username, u2.email
FROM user_connections uc
JOIN users u2
  ON u2.id = CASE
              WHEN uc.user_id = ? THEN uc.connection_id
              ELSE uc.user_id
            END
WHERE ? IN (uc.user_id, uc.connection_id)
  AND u2.username LIKE CONCAT(?, '%')
ORDER BY u2.username
LIMIT 20;
