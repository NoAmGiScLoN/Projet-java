package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.OrderDetail;

/**
 * DAO pour la gestion des commandes.
 */
public class OrderDAO {

    /**
     * Crée une commande avec ses détails dans la base.
     * Utilise une transaction pour assurer que tout est bien inséré.
     * @param order la commande à créer
     * @return true si réussite, false sinon
     */
    public boolean createOrder(Order order) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            String orderSQL = "INSERT INTO Commandes(utilisateur_id, date, comment) VALUES (?, ?, ?)";
            try (PreparedStatement orderStmt = con.prepareStatement(orderSQL, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getUserId());
                orderStmt.setTimestamp(2, order.getDate());
                orderStmt.setString(3, order.getComment());
                int affected = orderStmt.executeUpdate();
                if (affected == 0) {
                    con.rollback();
                    return false;
                }
                ResultSet keys = orderStmt.getGeneratedKeys();
                if (keys.next()) {
                    int orderId = keys.getInt(1);
                    order.setId(orderId);
                    String detailSQL = "INSERT INTO DetailsCommande(commande_id, produit_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement detailStmt = con.prepareStatement(detailSQL)) {
                        for (OrderDetail detail : order.getDetails()) {
                            detailStmt.setInt(1, orderId);
                            detailStmt.setInt(2, detail.getProductId());
                            detailStmt.setInt(3, detail.getQuantity());
                            detailStmt.setDouble(4, detail.getUnitPrice());
                            detailStmt.executeUpdate();
                        }
                    }
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    /**
     * Récupère la liste des commandes pour un utilisateur donné.
     * @param userId identifiant du client
     * @return liste des commandes passées par le client
     */
    public List<Order> getOrdersByUser(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Commandes WHERE utilisateur_id = ? ORDER BY date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            OrderDetailDAO detailDAO = new OrderDetailDAO();
            while (rs.next()) {
                int orderId = rs.getInt("id");
                Timestamp date = rs.getTimestamp("date");
                String comment = rs.getString("comment");
                Order order = new Order(orderId, userId, date, comment);
                order.setDetails(detailDAO.getDetailsByOrderId(orderId));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
