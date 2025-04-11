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

-- Création de la table panier (un panier par utilisateur)
CREATE TABLE IF NOT EXISTS panier (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      id_utilisateur INT NOT NULL,
                                      date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
                                      FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE
    );

-- Lignes de panier (produits ajoutés au panier)
CREATE TABLE IF NOT EXISTS ligne_panier (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            id_panier INT NOT NULL,
                                            id_produit INT NOT NULL,
                                            quantite INT NOT NULL,
                                            FOREIGN KEY (id_panier) REFERENCES panier(id) ON DELETE CASCADE,
    FOREIGN KEY (id_produit) REFERENCES produit(id) ON DELETE CASCADE
    );

-- Création de la table facture
CREATE TABLE IF NOT EXISTS facture (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       id_utilisateur INT NOT NULL,
                                       date_facture DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       montant_total DOUBLE NOT NULL,
                                       FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id) ON DELETE CASCADE
    );

-- Lignes de facture (détail des produits facturés)
CREATE TABLE IF NOT EXISTS ligne_facture (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             id_facture INT NOT NULL,
                                             id_produit INT NOT NULL,
                                             quantite INT NOT NULL,
                                             prix_unitaire DOUBLE NOT NULL,
                                             FOREIGN KEY (id_facture) REFERENCES facture(id) ON DELETE CASCADE,
    FOREIGN KEY (id_produit) REFERENCES produit(id)
    );
