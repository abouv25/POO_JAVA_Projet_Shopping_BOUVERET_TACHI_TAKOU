# Projet Java Shopping B-T-T

## Description :
Ce projet est une application de boutique en ligne réalisée en Java (POO + Swing) avec une base de données MySQL.
Il permet la gestion d'utilisateurs, la connexion/inscription, la consultation de produits, la gestion du panier, la création de factures, l'administration des utilisateurs, des statistiques de vente, et la gestion de réductions.

## Comment installer :
1. Cloner ou dézipper ce projet sur votre ordinateur.
2. Ouvrir le projet avec IntelliJ IDEA (ou un IDE Java équivalent).
3. Vérifier que le dossier `/lib/` contient bien toutes les librairies nécessaires (.jar) :
   - `mysql-connector-java-xxx.jar` (connexion MySQL)
   - `openpdf-xxx.jar` (export PDF)
   - `jfreechart-xxx.jar` (graphes statistiques)
4. Importer le fichier `shopping.sql` contenu dans `/sql/` dans votre base de données MySQL locale.
5. Vérifier que les identifiants de connexion à la base de données sont corrects dans `ConnexionBD.java`.

## Comment lancer :
- Double-cliquer sur le fichier `shopping-btt.jar` (dans le dossier `/out/`) pour lancer l'application en mode graphique.
- Ou bien, lancer depuis l'IDE IntelliJ avec la classe `MainWindow.java`.

## Identifiants test :
- Utilisateur : `TestUser@mail.com` / Mot de passe : `123456`
- Administrateur : `TestAdmin@mail.com` / Mot de passe : `123456`

## Sources utilisées :
- Composants graphiques Swing : documentation officielle Java
- Gestion PDF : Librairie OpenPDF
- Graphiques : Librairie JFreeChart
- Requêtes SQL adaptées depuis https://www.mysqltutorial.org/
- Structures de code inspirées de BoostCamp Java 2025 (site pédagogique)

## Auteurs :
- Antoine Bouveret

## Date :
27/04/2025
