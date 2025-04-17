
-- 🔁 Supprimer la table utilisateur si elle existe déjà
DROP TABLE IF EXISTS utilisateur;

-- 👤 Création de la table utilisateur
CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- Identifiant unique
    nom VARCHAR(100) NOT NULL,                       -- Nom de famille
    prenom VARCHAR(100) NOT NULL,                    -- Prénom
    email VARCHAR(150) NOT NULL UNIQUE,              -- Email unique
    motDePasse VARCHAR(255) NOT NULL,                -- Mot de passe (hashé de préférence)
    estAdmin BOOLEAN DEFAULT FALSE                   -- True si admin, False si client
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 🔁 Supprimer la table facture si elle existe déjà
DROP TABLE IF EXISTS facture;

-- 🧾 Création de la table facture
CREATE TABLE facture (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- ID de la facture
    utilisateur_id INT NOT NULL,                     -- Clé étrangère vers l’utilisateur
    dateFacture DATETIME DEFAULT CURRENT_TIMESTAMP,  -- Date de création
    total DOUBLE NOT NULL CHECK (total >= 0),        -- Total de la commande
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
