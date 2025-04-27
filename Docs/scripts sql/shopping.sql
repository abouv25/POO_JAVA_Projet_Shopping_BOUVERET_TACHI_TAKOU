-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3308
-- Généré le : dim. 27 avr. 2025 à 22:35
-- Version du serveur : 8.3.0
-- Version de PHP : 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `shopping`
--

DELIMITER $$
--
-- Procédures
--
DROP PROCEDURE IF EXISTS `genererProduitsAleatoires`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `genererProduitsAleatoires` (IN `nombre` INT)   BEGIN
    DECLARE compteur INT DEFAULT 0;

    WHILE compteur < nombre DO
        INSERT INTO produit (nom, prix, quantiteStock) VALUES (
            CONCAT('Produit_', FLOOR(RAND() * 1000)),
            ROUND((RAND() * 100) + 1, 2),
            FLOOR((RAND() * 100) + 1)
        );
        SET compteur = compteur + 1;
    END WHILE;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `facture`
--

DROP TABLE IF EXISTS `facture`;
CREATE TABLE IF NOT EXISTS `facture` (
  `id` int NOT NULL AUTO_INCREMENT,
  `utilisateur_id` int NOT NULL,
  `dateFacture` datetime DEFAULT CURRENT_TIMESTAMP,
  `total` double NOT NULL,
  `date_facture` date DEFAULT NULL,
  `montant_total` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `utilisateur_id` (`utilisateur_id`)
) ;

--
-- Déchargement des données de la table `facture`
--

INSERT INTO `facture` (`id`, `utilisateur_id`, `dateFacture`, `total`, `date_facture`, `montant_total`) VALUES
(2, 1, '2025-03-18 16:37:22', 67.41, NULL, NULL),
(3, 2, '2025-04-16 16:37:22', 111.55, NULL, NULL),
(4, 1, '2025-03-26 16:37:22', 138.69, NULL, NULL),
(5, 2, '2025-03-24 16:37:22', 74.17, NULL, NULL),
(6, 1, '2025-03-30 16:37:22', 165.38, NULL, NULL),
(7, 1, '2025-04-16 16:37:22', 32.37, NULL, NULL),
(8, 2, '2025-04-03 16:37:22', 231.09, NULL, NULL),
(9, 2, '2025-04-12 16:37:22', 175.18, NULL, NULL),
(10, 1, '2025-03-27 16:37:22', 96.58, NULL, NULL),
(11, 2, '2025-03-31 16:37:22', 59.35, NULL, NULL),
(12, 2, '2025-03-22 16:37:22', 171.53, NULL, NULL),
(13, 1, '2025-04-11 16:37:22', 149.9, NULL, NULL),
(14, 1, '2025-04-01 16:37:22', 27.09, NULL, NULL),
(15, 2, '2025-03-21 16:37:22', 149.43, NULL, NULL),
(16, 2, '2025-04-12 16:37:22', 181.22, NULL, NULL),
(17, 1, '2025-03-27 16:37:22', 86.7, NULL, NULL),
(18, 2, '2025-04-03 16:37:22', 72.76, NULL, NULL),
(19, 1, '2025-03-24 16:37:22', 56.64, NULL, NULL),
(20, 1, '2025-03-30 16:37:22', 117.39, NULL, NULL),
(21, 2, '2025-03-18 16:37:22', 103.45, NULL, NULL),
(301, 201, '2021-05-15 10:00:00', 120.5, NULL, NULL),
(302, 202, '2022-07-20 14:30:00', 85, NULL, NULL),
(303, 203, '2023-03-12 09:45:00', 99.99, NULL, NULL),
(304, 204, '2024-01-05 16:20:00', 150, NULL, NULL),
(305, 205, '2025-02-17 11:15:00', 200, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `facture_produit`
--

DROP TABLE IF EXISTS `facture_produit`;
CREATE TABLE IF NOT EXISTS `facture_produit` (
  `id_facture` int NOT NULL,
  `id_produit` int NOT NULL,
  `quantite` int NOT NULL,
  KEY `id_facture` (`id_facture`),
  KEY `id_produit` (`id_produit`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `facture_produit`
--

INSERT INTO `facture_produit` (`id_facture`, `id_produit`, `quantite`) VALUES
(1, 1, 2),
(1, 2, 1);

-- --------------------------------------------------------

--
-- Structure de la table `ligne_facture`
--

DROP TABLE IF EXISTS `ligne_facture`;
CREATE TABLE IF NOT EXISTS `ligne_facture` (
  `id` int NOT NULL AUTO_INCREMENT,
  `facture_id` int NOT NULL,
  `produit_id` int NOT NULL,
  `quantite` int NOT NULL,
  `prixUnitaire` double NOT NULL,
  `prix_unitaire` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `facture_id` (`facture_id`),
  KEY `produit_id` (`produit_id`)
) ;

--
-- Déchargement des données de la table `ligne_facture`
--

INSERT INTO `ligne_facture` (`id`, `facture_id`, `produit_id`, `quantite`, `prixUnitaire`, `prix_unitaire`) VALUES
(401, 301, 1, 2, 76.34, NULL),
(402, 301, 5, 1, 60.86, NULL),
(403, 302, 2, 3, 39.94, NULL),
(404, 302, 8, 2, 78.19, NULL),
(405, 303, 3, 1, 109.03, NULL),
(406, 303, 9, 1, 64.04, NULL),
(407, 304, 4, 2, 59.54, NULL),
(408, 304, 6, 1, 143.63, NULL),
(409, 305, 7, 1, 93.7, NULL),
(410, 305, 10, 1, 99.54, NULL),
(411, 301, 1, 2, 76.34, NULL),
(412, 301, 2, 1, 39.94, NULL),
(413, 302, 3, 3, 109.03, NULL),
(414, 302, 4, 1, 59.54, NULL),
(415, 303, 5, 2, 89.99, NULL),
(416, 303, 6, 1, 144.29, NULL),
(417, 304, 7, 5, 93.7, NULL),
(418, 304, 8, 1, 78.19, NULL),
(419, 305, 9, 2, 64.04, NULL),
(420, 305, 10, 1, 99.54, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `ligne_panier`
--

DROP TABLE IF EXISTS `ligne_panier`;
CREATE TABLE IF NOT EXISTS `ligne_panier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_panier` int NOT NULL,
  `id_produit` int NOT NULL,
  `quantite` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_panier` (`id_panier`),
  KEY `id_produit` (`id_produit`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `panier`
--

DROP TABLE IF EXISTS `panier`;
CREATE TABLE IF NOT EXISTS `panier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `utilisateur_id` int NOT NULL,
  `dateCreation` datetime DEFAULT CURRENT_TIMESTAMP,
  `estValide` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `utilisateur_id` (`utilisateur_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

DROP TABLE IF EXISTS `produit`;
CREATE TABLE IF NOT EXISTS `produit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prix` double NOT NULL,
  `quantiteStock` int NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `idReduction` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`id`, `nom`, `prix`, `quantiteStock`, `image`, `idReduction`) VALUES
(1, 'T-shirt Coton Bio', 76.34, 100, 'Vue/images/produit1.png', NULL),
(2, 'Sweat Hoodie Bleu', 39.94, 41, 'Vue/images/produit2.png', NULL),
(3, 'Casquette Stylée', 109.03, 16, 'Vue/images/produit3.png', NULL),
(4, 'Jean Slim Noir', 59.54, 72, 'Vue/images/produit4.png', NULL),
(5, 'Sac à dos urbain', 60.86, 50, 'Vue/images/produit5.png', NULL),
(6, 'Montre Digitale', 143.63, 21, 'Vue/images/produit6.png', NULL),
(7, 'Lunettes de soleil', 93.7, 61, 'Vue/images/produit7.png', NULL),
(8, 'Chaussures Running', 78.19, 96, 'Vue/images/produit8.png', NULL),
(9, 'Ceinture Cuir', 64.04, 31, 'Vue/images/produit9.png', NULL),
(10, 'Gourde Inox', 99.54, 28, 'Vue/images/produit10.png', NULL),
(11, 'Pantalon Chino', 149.27, 37, 'Vue/images/produit11.png', NULL),
(12, 'Chemise Oxford', 129.47, 91, 'Vue/images/produit12.png', NULL),
(13, 'Pull Cachemire', 144.29, 73, 'Vue/images/produit13.png', NULL),
(14, 'Blouson Cuir', 27.35, 47, 'Vue/images/produit14.png', NULL),
(15, 'Parka Hiver', 64.71, 28, 'Vue/images/produit15.png', NULL),
(16, 'Short Sport', 128.7, 42, 'Vue/images/produit16.png', NULL),
(17, 'Maillot de bain', 122.77, 53, 'Vue/images/produit17.png', NULL),
(18, 'Chaussures en toile', 110.71, 81, 'Vue/images/produit18.png', NULL),
(19, 'Bonnet Hiver', 25.12, 24, 'Vue/images/produit19.png', NULL),
(20, 'Gants Cuir', 69.98, 30, 'Vue/images/produit20.png', NULL),
(21, 'Polo Classique', 59.2, 29, 'Vue/images/produit21.png', NULL),
(22, 'Mocassins', 35.31, 33, 'Vue/images/produit22.png', NULL),
(23, 'Sandales Été', 130.12, 27, 'Vue/images/produit23.png', NULL),
(24, 'Montre Sport', 135.93, 42, 'Vue/images/produit24.png', NULL),
(25, 'Portefeuille Cuir', 73.59, 15, 'Vue/images/produit25.png', NULL),
(26, 'Sac bandoulière', 43.07, 39, 'Vue/images/produit26.png', NULL),
(27, 'Chapeau Panama', 15.8, 61, 'Vue/images/produit27.png', NULL),
(28, 'Sweat à capuche', 93.31, 69, 'Vue/images/produit28.png', NULL),
(29, 'Débardeur', 57.52, 38, 'Vue/images/produit29.png', NULL),
(30, 'Jean Droit', 134.53, 12, 'Vue/images/produit30.png', NULL),
(31, 'T-shirt Coton Bio', 76.34, 100, 'Vue/images/produit1.png', NULL),
(32, 'Sweat Hoodie Bleu', 39.94, 41, 'Vue/images/produit2.png', NULL),
(33, 'Casquette Stylée', 109.03, 16, 'Vue/images/produit3.png', NULL),
(34, 'Jean Slim Noir', 59.54, 72, 'Vue/images/produit4.png', NULL),
(35, 'Sac à dos urbain', 60.86, 50, 'Vue/images/produit5.png', NULL),
(36, 'Montre Digitale', 143.63, 21, 'Vue/images/produit6.png', NULL),
(37, 'Lunettes de soleil', 93.7, 61, 'Vue/images/produit7.png', NULL),
(38, 'Chaussures Running', 78.19, 96, 'Vue/images/produit8.png', NULL),
(39, 'Ceinture Cuir', 64.04, 31, 'Vue/images/produit9.png', NULL),
(40, 'Gourde Inox', 99.54, 28, 'Vue/images/produit10.png', NULL),
(41, 'Pantalon Chino', 149.27, 37, 'Vue/images/produit11.png', NULL),
(42, 'Chemise Oxford', 129.47, 91, 'Vue/images/produit12.png', NULL),
(43, 'Pull Cachemire', 144.29, 73, 'Vue/images/produit13.png', NULL),
(44, 'Blouson Cuir', 27.35, 47, 'Vue/images/produit14.png', NULL),
(45, 'Parka Hiver', 64.71, 28, 'Vue/images/produit15.png', NULL),
(46, 'Short Sport', 128.7, 42, 'Vue/images/produit16.png', NULL),
(47, 'Maillot de bain', 122.77, 53, 'Vue/images/produit17.png', NULL),
(48, 'Chaussures en toile', 110.71, 81, 'Vue/images/produit18.png', NULL),
(49, 'Bonnet Hiver', 25.12, 24, 'Vue/images/produit19.png', NULL),
(50, 'Gants Cuir', 69.98, 30, 'Vue/images/produit20.png', NULL),
(51, 'Polo Classique', 59.2, 29, 'Vue/images/produit21.png', NULL),
(52, 'Mocassins', 35.31, 33, 'Vue/images/produit22.png', NULL),
(53, 'Sandales Été', 130.12, 27, 'Vue/images/produit23.png', NULL),
(54, 'Montre Sport', 135.93, 42, 'Vue/images/produit24.png', NULL),
(55, 'Portefeuille Cuir', 73.59, 15, 'Vue/images/produit25.png', NULL),
(56, 'Sac bandoulière', 43.07, 39, 'Vue/images/produit26.png', NULL),
(57, 'Chapeau Panama', 15.8, 61, 'Vue/images/produit27.png', NULL),
(58, 'Sweat à capuche', 93.31, 69, 'Vue/images/produit28.png', NULL),
(59, 'Débardeur', 57.52, 38, 'Vue/images/produit29.png', NULL),
(60, 'Jean Droit', 134.53, 12, 'Vue/images/produit30.png', NULL),
(61, 'T-shirt Coton Bio', 76.34, 100, 'Vue/images/produit1.png', NULL),
(62, 'Sweat Hoodie Bleu', 39.94, 41, 'Vue/images/produit2.png', NULL),
(63, 'Casquette Stylée', 109.03, 16, 'Vue/images/produit3.png', NULL),
(64, 'Jean Slim Noir', 59.54, 72, 'Vue/images/produit4.png', NULL),
(65, 'Sac à dos urbain', 60.86, 50, 'Vue/images/produit5.png', NULL),
(66, 'Montre Digitale', 143.63, 21, 'Vue/images/produit6.png', NULL),
(67, 'Lunettes de soleil', 93.7, 61, 'Vue/images/produit7.png', NULL),
(68, 'Chaussures Running', 78.19, 96, 'Vue/images/produit8.png', NULL),
(69, 'Ceinture Cuir', 64.04, 31, 'Vue/images/produit9.png', NULL),
(70, 'Gourde Inox', 99.54, 28, 'Vue/images/produit10.png', NULL),
(71, 'Pantalon Chino', 149.27, 37, 'Vue/images/produit11.png', NULL),
(72, 'Chemise Oxford', 129.47, 91, 'Vue/images/produit12.png', NULL),
(73, 'Pull Cachemire', 144.29, 73, 'Vue/images/produit13.png', NULL),
(74, 'Blouson Cuir', 27.35, 47, 'Vue/images/produit14.png', NULL),
(75, 'Parka Hiver', 64.71, 28, 'Vue/images/produit15.png', NULL),
(76, 'Short Sport', 128.7, 42, 'Vue/images/produit16.png', NULL),
(77, 'Maillot de bain', 122.77, 53, 'Vue/images/produit17.png', NULL),
(78, 'Chaussures en toile', 110.71, 81, 'Vue/images/produit18.png', NULL),
(79, 'Bonnet Hiver', 25.12, 24, 'Vue/images/produit19.png', NULL),
(80, 'Gants Cuir', 69.98, 30, 'Vue/images/produit20.png', NULL),
(81, 'Polo Classique', 59.2, 29, 'Vue/images/produit21.png', NULL),
(82, 'Mocassins', 35.31, 33, 'Vue/images/produit22.png', NULL),
(83, 'Sandales Été', 130.12, 27, 'Vue/images/produit23.png', NULL),
(84, 'Montre Sport', 135.93, 42, 'Vue/images/produit24.png', NULL),
(85, 'Portefeuille Cuir', 73.59, 15, 'Vue/images/produit25.png', NULL),
(86, 'Sac bandoulière', 43.07, 39, 'Vue/images/produit26.png', NULL),
(87, 'Chapeau Panama', 15.8, 61, 'Vue/images/produit27.png', NULL),
(88, 'Sweat à capuche', 93.31, 69, 'Vue/images/produit28.png', NULL),
(89, 'Débardeur', 57.52, 38, 'Vue/images/produit29.png', NULL),
(90, 'Jean Droit', 134.53, 12, 'Vue/images/produit30.png', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `questions_securite`
--

DROP TABLE IF EXISTS `questions_securite`;
CREATE TABLE IF NOT EXISTS `questions_securite` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `question1` varchar(255) DEFAULT NULL,
  `reponse1` varchar(255) DEFAULT NULL,
  `question2` varchar(255) DEFAULT NULL,
  `reponse2` varchar(255) DEFAULT NULL,
  `question3` varchar(255) DEFAULT NULL,
  `reponse3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `email` (`email`(250))
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `questions_securite`
--

INSERT INTO `questions_securite` (`id`, `email`, `question1`, `reponse1`, `question2`, `reponse2`, `question3`, `reponse3`) VALUES
(1, 'antoine@test.com', 'Nom de votre premier animal ?', 'andy', 'Nom de votre premier animal ?', 'andy', 'Nom de votre premier animal ?', 'andy'),
(2, 'antoine2@test.com', 'Votre professeur préféré ?', 'bobo', 'Nom de votre premier animal ?', 'bobo', 'Nom de votre premier animal ?', 'bobo'),
(3, 'test1@test.com', 'Nom de votre premier animal ?', 'dodo', 'Nom de votre premier animal ?', 'dodo', 'Nom de votre premier animal ?', 'dodo'),
(4, 'u@test.com', 'Nom de votre premier animal ?', 'dodo', 'Nom de votre premier animal ?', 'dodo', 'Nom de votre premier animal ?', 'dodo'),
(5, 'a@test.com', 'Nom de votre premier animal ?', 'dodo', 'Nom de votre premier animal ?', 'dodo', 'Nom de votre premier animal ?', 'dodo');

-- --------------------------------------------------------

--
-- Structure de la table `reduction`
--

DROP TABLE IF EXISTS `reduction`;
CREATE TABLE IF NOT EXISTS `reduction` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(255) NOT NULL,
  `pourcentage` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `reduction`
--

INSERT INTO `reduction` (`id`, `nom`, `pourcentage`) VALUES
(1, 'Promo Été', 15),
(2, 'Fidélité Client', 10),
(3, 'Black Friday', 30);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `motDePasse` varchar(255) NOT NULL,
  `estAdmin` tinyint(1) DEFAULT '0',
  `clientFidele` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `nom`, `prenom`, `email`, `motDePasse`, `estAdmin`, `clientFidele`) VALUES
(1, 'User', 'Test', 'TestUser@mail.com', '123456', 0, 0),
(2, 'Admin', 'Test', 'TestAdmin@mail.com', '123456', 1, 0),
(3, 'Bouveret', 'Antoine', 'bouveret.antoine@gmail.com', 'Bouveret123456', 1, 1),
(4, '', '', 'antoine@test.com', '', 1, 1),
(5, '', '', 'antoine2@test.com', '', 1, 1),
(6, 'Durand', 'Alice', 'alice1@example.com', 'password', 0, 1),
(7, 'Martin', 'Bob', 'bob1@example.com', 'password', 0, 0),
(8, 'Petit', 'Claire', 'claire1@example.com', 'password', 0, 1),
(9, 'Lemoine', 'David', 'david1@example.com', 'password', 0, 0),
(10, 'Roux', 'Eva', 'eva1@example.com', 'password', 0, 0),
(11, 'Morel', 'Fabrice', 'fabrice1@example.com', 'password', 0, 1),
(12, 'Bernard', 'Gina', 'gina1@example.com', 'password', 0, 1),
(13, 'Henry', 'Hugo', 'hugo1@example.com', 'password', 0, 0),
(14, 'Robert', 'Isabelle', 'isabelle1@example.com', 'password', 0, 1),
(15, 'Bertrand', 'Julien', 'julien1@example.com', 'password', 0, 0),
(16, 'Lefevre', 'Karine', 'karine1@example.com', 'password', 0, 0),
(17, 'Dubois', 'Louis', 'louis1@example.com', 'password', 0, 1),
(18, 'Faure', 'Morgane', 'morgane1@example.com', 'password', 0, 0),
(19, 'Lopez', 'Nicolas', 'nicolas1@example.com', 'password', 0, 1),
(20, 'Marchand', 'Olivia', 'olivia1@example.com', 'password', 0, 0),
(21, 'Garnier', 'Paul', 'paul1@example.com', 'password', 0, 1),
(22, 'Perrot', 'Quentin', 'quentin1@example.com', 'password', 0, 1),
(23, 'Guichard', 'Romain', 'romain1@example.com', 'password', 0, 0),
(24, 'Benoit', 'Sarah', 'sarah1@example.com', 'password', 0, 1),
(25, 'Menard', 'Thomas', 'thomas1@example.com', 'password', 0, 0),
(101, 'Jean Dupont', '', 'jean.dupont@example.com', 'pass123', 0, 1),
(102, 'Alice Martin', '', 'alice.martin@example.com', 'pass123', 0, 0),
(103, 'Louis Bernard', '', 'louis.bernard@example.com', 'pass123', 0, 1),
(104, 'Emma Moreau', '', 'emma.moreau@example.com', 'pass123', 0, 0),
(105, 'Paul Dubois', '', 'paul.dubois@example.com', 'pass123', 0, 1),
(201, 'Lucas', 'Martin', 'lucas.martin@example.com', 'password', 0, 1),
(202, 'Sophie', 'Durand', 'sophie.durand@example.com', 'password', 0, 0),
(203, 'Maxime', 'Petit', 'maxime.petit@example.com', 'password', 0, 1),
(204, 'Emma', 'Lemoine', 'emma.lemoine@example.com', 'password', 0, 0),
(205, 'Nathan', 'Roux', 'nathan.roux@example.com', 'password', 0, 1),
(206, 'boubou', 'Anto', 'test1@test.com', '123456', 1, 1),
(207, 'a', 'a', 'u@test.com', '123456', 0, 0),
(208, 'a', 'a', 'a@test.com', '123456', 1, 1);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `facture`
--
ALTER TABLE `facture`
  ADD CONSTRAINT `facture_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `ligne_facture`
--
ALTER TABLE `ligne_facture`
  ADD CONSTRAINT `ligne_facture_ibfk_1` FOREIGN KEY (`facture_id`) REFERENCES `facture` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ligne_facture_ibfk_2` FOREIGN KEY (`produit_id`) REFERENCES `produit` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `panier`
--
ALTER TABLE `panier`
  ADD CONSTRAINT `panier_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
