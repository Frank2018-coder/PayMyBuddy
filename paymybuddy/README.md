## Modèle Physique de Données (MPD)

Le modèle est basé sur le diagramme UML fourni : `User` et `Transaction`, avec une relation "connections" (liste d'amis) entre utilisateurs.

### Tables

#### `users`
- Stocke les comptes utilisateurs.
- Contraintes : `username` et `email` uniques.
- `password_hash` contient un mot de passe **haché** (ex: BCrypt), jamais stocké en clair.

Colonnes principales :
- id (PK)
- username (UNIQUE)
- email (UNIQUE)
- password_hash
- created_at

#### `user_connections`
- Implémente la relation many-to-many auto-référente : un utilisateur peut avoir plusieurs connexions (amis).
- Clé primaire composite : (user_id, connection_id)
- Clés étrangères vers `users(id)`.

#### `transactions`
- Stocke les transferts d'argent.
- Deux relations vers `users` :
    - sender_id (émetteur)
    - receiver_id (receveur)
- `amount` est stocké en `DECIMAL(12,2)` pour éviter les erreurs d'arrondi (contrairement à double/float).
- Index ajoutés pour accélérer l'historique par utilisateur.

### Contraintes métiers gérées côté service (Spring)
MySQL n’étant pas toujours strict sur les contraintes `CHECK` selon les versions/configurations, les règles suivantes sont validées dans la couche service Spring :
- montant strictement positif
- interdiction d'auto-virement (sender_id != receiver_id)
- interdiction d'auto-connexion (user_id != connection_id)
- prévention des doublons de connexions (A,B) / (B,A)

Les scripts SQL sont disponibles dans :
- `sql/paymybuddy.sql`
- `sql/insert.sql`
- `sql/queries.sql`
