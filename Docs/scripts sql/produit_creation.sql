
-- 🔁 Supprimer la table si elle existe déjà (sécurisé)
DROP TABLE IF EXISTS produit;

-- 🧱 Création de la table produit
CREATE TABLE produit (
    id INT AUTO_INCREMENT PRIMARY KEY,               -- Identifiant unique du produit
    nom VARCHAR(100) NOT NULL,                       -- Nom du produit (ex : T-shirt, Casquette)
    prix DOUBLE NOT NULL CHECK (prix >= 0),          -- Prix en euros, toujours positif
    quantiteStock INT NOT NULL CHECK (quantiteStock >= 0), -- Quantité en stock, pas de valeurs négatives
    image VARCHAR(255)                               -- Chemin de l’image (ex : 'Vue/images/tshirt.png')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
