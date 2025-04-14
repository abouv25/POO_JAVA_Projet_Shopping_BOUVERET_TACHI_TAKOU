package DAO;

import modele.Produit;

import java.sql.*;

public class ProduitDAO {

    // Méthode pour récupérer un produit à partir d'un ResultSet
    public Produit resultSetToProduit(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nom = rs.getString("nom");
        double prix = rs.getDouble("prix");
        int quantite = rs.getInt("quantite");  // Assurez-vous que ces champs existent dans votre table
        return new Produit(id, nom, prix, quantite);
    }
}
