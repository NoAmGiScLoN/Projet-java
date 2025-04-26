package dao;

import java.sql.*;
import model.Admin;
import model.Client;
import model.User;

/**
 * DAO pour les opérations sur les utilisateurs (connexion et inscription).
 */
public class UserDAO {

    /**
     * Authentifie un utilisateur en vérifiant son email et son mot de passe.
     * @param email email de l'utilisateur
     * @param password mot de passe
     * @return un objet User (Client ou Admin) si l’authentification réussit, sinon null
     */
    public User login(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM Utilisateurs WHERE email = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                String role = rs.getString("role");
                if ("admin".equalsIgnoreCase(role)) {
                    user = new Admin(id, name, email, password);
                } else {
                    user = new Client(id, name, email, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Inscrit un nouvel utilisateur en tant que client.
     * @param name nom de l'utilisateur
     * @param email email (unique)
     * @param password mot de passe
     * @return true si l'inscription est réussie, false sinon
     */
    public boolean register(String name, String email, String password) {
        String checkSql = "SELECT id FROM Utilisateurs WHERE email = ?";
        String insertSql = "INSERT INTO Utilisateurs(nom, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkStmt = con.prepareStatement(checkSql);
             PreparedStatement insertStmt = con.prepareStatement(insertSql)) {
            // Vérifier si l'email est déjà utilisé
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // Email déjà utilisé
            }
            // Insertion du nouvel utilisateur avec le rôle "client"
            insertStmt.setString(1, name);
            insertStmt.setString(2, email);
            insertStmt.setString(3, password);
            insertStmt.setString(4, "client");
            insertStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
