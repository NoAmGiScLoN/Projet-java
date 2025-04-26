package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour la connexion à la base de données MySQL.
 * Elle charge le driver JDBC et fournit une méthode pour obtenir une connexion.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:8889/ShoppingDB?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Adaptez ce champ selon votre configuration

    static {
        try {
            // Charger le driver JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fournit une connexion à la base de données.
     * @return Connection JDBC
     * @throws SQLException en cas d’erreur de connexion
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

