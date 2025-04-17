
# ✅ Plan de Test Global - Application Shopping B-T-T

## 1. Démarrage (VueAccueil)
- [ ] Le logo s’affiche bien en haut (image).
- [ ] Si aucun utilisateur connecté :
  - "Bienvenue, invité !" s'affiche.
  - "Connexion" est visible.
  - "Déconnexion" et "Historique" sont **cachés**.
- [ ] Cliquer sur le logo redirige vers l’accueil.

## 2. Connexion / Déconnexion
- [ ] Cliquer sur "Connexion" → accéder à `ConnexionView`.
- [ ] Se connecter avec un utilisateur existant.
- [ ] Retour à l’accueil : nom de l’utilisateur affiché en haut à droite.
- [ ] Boutons "Historique", "Panier", "Déconnexion" visibles.
- [ ] Cliquer sur "Déconnexion" remet en mode invité.

## 3. Catalogue Produits
- [ ] Cliquer sur "Catalogue Produits" → affiche bien `VueProduits`.
- [ ] Logo + nom utilisateur s'affichent en haut.
- [ ] Produits listés correctement.
- [ ] Bouton "Ajouter au panier" fonctionne (tester un ajout).
- [ ] Quantité 0 affiche la boîte de suppression.

## 4. Panier
- [ ] Cliquer sur "Voir le panier" ou bouton accueil → `VuePanier`.
- [ ] Chaque ligne a les boutons + / – et champ de quantité modifiable.
- [ ] Le bouton "Mettre à jour" fonctionne et affiche la confirmation.
- [ ] Total mis à jour en direct.

## 5. Validation → VueNouvelleFacture
- [ ] Cliquer sur "Valider la commande".
- [ ] La vue `VueNouvelleFacture` affiche les bons produits.
- [ ] Remise 10% si client fidèle.
- [ ] Cliquer sur "Confirmer" : message OK, retour à l’accueil.

## 6. Historique
- [ ] Cliquer sur "Historique de factures".
- [ ] Tri, filtre 2025 fonctionnent.
- [ ] Double-clic sur une ligne ou bouton "Voir les détails" → `VueDetailFacture`.
- [ ] Total affiché.
- [ ] Bouton "Exporter en PDF" fonctionne et ouvre le fichier.

## 7. Barre Supérieure
Sur **toutes les vues**, vérifier que :
- [ ] Le logo en haut à gauche est bien visible et **cliquable** vers l’accueil.
- [ ] Le nom de l’utilisateur (ou "S'identifier") est visible en haut à droite.
