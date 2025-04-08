# 📦 Configuration de la Base de Données MySQL – Projet Shopping

## 🔑 Identifiants de Connexion

- **Hôte** : `localhost`
- **Port** : `3308`
- **Base de données** : `shopping`
- **Utilisateur** : `root`
- **Mot de passe** : *(laisser vide si aucun)*

> ⚠️ N'oubliez pas d'adapter ces valeurs dans la classe `ConnexionBD.java` :
> 
> ```java
> private static final String URL = "jdbc:mysql://localhost:3008/shopping";
> ```

---

## 🛠️ Script SQL – Création de la base et de la table `produit`

```sql
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
```
