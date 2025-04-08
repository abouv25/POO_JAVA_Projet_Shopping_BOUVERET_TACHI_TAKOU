-- Création de la base de données
CREATE DATABASE IF NOT EXISTS shopping;
USE shopping;

-- Création de la table produit
CREATE TABLE IF NOT EXISTS produit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prix DOUBLE NOT NULL,
    quantiteStock INT NOT NULL
);
