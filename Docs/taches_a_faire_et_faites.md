
# Comparaison des tâches :

| **Tâche**                                           | **Statut**       | **Détails**                                                     |
|-----------------------------------------------------|------------------|----------------------------------------------------------------- |
| **Gestion des produits**                            | ✅ Terminé        | Ajout, récupération, suppression avec `ProduitDAO`              |
| **Gestion des utilisateurs**                       | ✅ Terminé        | Inscription et connexion avec `UtilisateurDAO`                  |
| **Création et gestion du panier**                   | ✅ Terminé        | Ajout, modification, suppression des produits avec `PanierDAO`  |
| **Création de la facture à partir du panier**       | ✅ Terminé        | Utilisation de `FactureDAO` pour créer des factures             |
| **Affichage de l'historique des factures**          | ✅ Terminé        | Vue `VueHistoriqueFactures` affichant l'historique des factures |
| **Affichage des détails de la facture**             | ✅ Terminé        | Vue `VueDetailFacture` pour afficher les produits d’une facture |
| **Export PDF de la facture**                        | ✅ Terminé        | Génération et ouverture automatique du PDF                      |
| **Personnalisation du PDF**                         | ✅ Terminé        | Logo et informations clients ajoutés dans le PDF                |
| **Section "Merci de votre commande" dans le PDF**   | ✅ Terminé        | Ajouté dans l'export PDF                                        |
| **Filtrage des factures par année (2025)**          | ❓ À faire        | Ajouter un filtre pour afficher les factures de 2025            |
| **Bouton "Retour à l’accueil" dans le PDF**         | ❓ À faire        | Ajouter un bouton pour revenir à l'accueil depuis le PDF        |
| **Vue `MainWindow` pour la navigation**             | ❓ À faire        | Créer la vue principale avec navigation (Accueil / Produits / Factures) |
| **Vue `VuePanier` pour la gestion du panier**       | ❓ À faire        | Créer la vue pour ajouter/supprimer des produits du panier      |
| **Page de connexion utilisateur**                   | ❓ À faire        | Implémenter un écran de connexion avant l’accès aux vues principales |
| **Export groupé de plusieurs factures**             | ❓ Optionnel      | Ajouter une fonctionnalité d'export groupé des factures         |
| **Statistiques de vente par produit/client**        | ❓ Optionnel      | Créer des statistiques sur les ventes et produits               |
| **Tests unitaires (JUnit)**                         | ❓ Optionnel      | Ajouter des tests unitaires pour vérifier les fonctionnalités  |
