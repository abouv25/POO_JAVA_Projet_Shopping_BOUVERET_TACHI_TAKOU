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

-- Création de la table utilisateur
CREATE TABLE IF NOT EXISTS utilisateur (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    motDePasse VARCHAR(100) NOT NULL,
    clientFidele BOOLEAN NOT NULL
    );
