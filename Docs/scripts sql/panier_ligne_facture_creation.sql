
-- 🔁 Supprimer la table panier si elle existe déjà
DROP TABLE IF EXISTS panier;

-- 🛒 Création de la table panier
CREATE TABLE panier (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- ID du panier
    utilisateur_id INT NOT NULL,                     -- Clé étrangère vers l’utilisateur
    dateCreation DATETIME DEFAULT CURRENT_TIMESTAMP, -- Date de création du panier
    estValide BOOLEAN DEFAULT FALSE,                 -- Panier validé en commande ?
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 🔁 Supprimer la table ligne_facture si elle existe déjà
DROP TABLE IF EXISTS ligne_facture;

-- 🧾 Création de la table ligne_facture
CREATE TABLE ligne_facture (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- ID ligne unique
    facture_id INT NOT NULL,                         -- Référence vers la facture
    produit_id INT NOT NULL,                         -- Référence vers le produit
    quantite INT NOT NULL CHECK (quantite > 0),      -- Quantité commandée
    prixUnitaire DOUBLE NOT NULL CHECK (prixUnitaire >= 0), -- Prix au moment de la commande
    FOREIGN KEY (facture_id) REFERENCES facture(id)
        ON DELETE CASCADE,
    FOREIGN KEY (produit_id) REFERENCES produit(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
