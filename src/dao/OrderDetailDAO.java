package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.OrderDetail;
import model.Product;

/**
 * DAO pour la gestion des détails d'une commande.
 */
public class OrderDetailDAO {

    /**
     * Récupère les détails d'une commande à partir de son identifiant.
     * @param orderId identifiant de la commande
     * @return liste des détails correspondants
     */
    public List<OrderDetail> getDetailsByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT d.id, d.produit_id, d.quantite, d.prix_unitaire, p.nom, p.marque, p.prix " +
                "FROM DetailsCommande d JOIN Produits p ON d.produit_id = p.id " +
                "WHERE d.commande_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int detailId = rs.getInt("id");
                int productId = rs.getInt("produit_id");
                int quantity = rs.getInt("quantite");
                double unitPrice = rs.getDouble("prix_unitaire");
                String name = rs.getString("nom");
                String brand = rs.getString("marque");
                double price = rs.getDouble("prix");
                Product product = new Product(productId, name, brand, price);
                OrderDetail detail = new OrderDetail(detailId, orderId, productId, quantity, unitPrice);
                detail.setProduct(product);
                details.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }
}
