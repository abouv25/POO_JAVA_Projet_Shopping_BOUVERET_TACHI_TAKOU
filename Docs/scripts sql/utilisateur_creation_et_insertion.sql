
-- 🔁 Suppression de la table utilisateur si elle existe déjà
DROP TABLE IF EXISTS utilisateur;

-- 👤 Création de la table utilisateur avec champ `prenom`
CREATE TABLE utilisateur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    motDePasse VARCHAR(255) NOT NULL,
    estAdmin BOOLEAN DEFAULT FALSE
);

-- 👤 Insertion d'un utilisateur simple
INSERT INTO utilisateur (nom, prenom, email, motDePasse, estAdmin)
VALUES ('User', 'Test', 'TestUser@mail.com', '123456', FALSE);

-- 👨‍💼 Insertion d'un administrateur
INSERT INTO utilisateur (nom, prenom, email, motDePasse, estAdmin)
VALUES ('Admin', 'Test', 'TestAdmin@mail.com', '123456', TRUE);
