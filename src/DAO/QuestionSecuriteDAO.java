package DAO;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class QuestionSecuriteDAO {

    public boolean enregistrerQuestions(String email, Map<String, String> questionsReponses) {
        String sql = "INSERT INTO questions_securite (email, question1, reponse1, question2, reponse2, question3, reponse3) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, questionsReponses.get("question1"));
            stmt.setString(3, questionsReponses.get("reponse1"));
            stmt.setString(4, questionsReponses.get("question2"));
            stmt.setString(5, questionsReponses.get("reponse2"));
            stmt.setString(6, questionsReponses.get("question3"));
            stmt.setString(7, questionsReponses.get("reponse3"));

            int lignes = stmt.executeUpdate();
            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("Erreur enregistrement questions sécurité : " + e.getMessage());
            return false;
        }
    }

    public Map<String, String> recupererQuestions(String email) {
        String sql = "SELECT question1, reponse1, question2, reponse2, question3, reponse3 FROM questions_securite WHERE email = ?";
        Map<String, String> map = new HashMap<>();

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                map.put("question1", rs.getString("question1"));
                map.put("reponse1", rs.getString("reponse1"));
                map.put("question2", rs.getString("question2"));
                map.put("reponse2", rs.getString("reponse2"));
                map.put("question3", rs.getString("question3"));
                map.put("reponse3", rs.getString("reponse3"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur récupération questions sécurité : " + e.getMessage());
        }

        return map;
    }
    public boolean verifierQuestions(String email, String q1, String r1, String q2, String r2, String q3, String r3) {
        Map<String, String> infos = recupererQuestions(email);

        return infos.get("question1").equalsIgnoreCase(q1) && infos.get("reponse1").equalsIgnoreCase(r1)
                && infos.get("question2").equalsIgnoreCase(q2) && infos.get("reponse2").equalsIgnoreCase(r2)
                && infos.get("question3").equalsIgnoreCase(q3) && infos.get("reponse3").equalsIgnoreCase(r3);
    }

}
