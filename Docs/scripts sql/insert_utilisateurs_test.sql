
-- 👤 Insertion d'un utilisateur simple
INSERT INTO utilisateur (nom, prenom, email, motDePasse, estAdmin)
VALUES ('User', 'Test', 'TestUser@mail.com', '123456', FALSE);

-- 👨‍💼 Insertion d'un administrateur
INSERT INTO utilisateur (nom, prenom, email, motDePasse, estAdmin)
VALUES ('Admin', 'Test', 'TestAdmin@mail.com', '123456', TRUE);
