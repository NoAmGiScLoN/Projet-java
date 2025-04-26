package dao;

import java.sql.*;
import model.Discount;

/**
 * DAO pour la gestion des remises (promotions).
 */
public class DiscountDAO {

    /**
     * Récupère la remise d'un produit en fonction de son identifiant.
     * @param productId identifiant du produit
     * @return l'objet Discount si trouvé, sinon null
     */
    public Discount getDiscountByProductId(int productId) {
        Discount discount = null;
        String sql = "SELECT * FROM Reductions WHERE produit_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int threshold = rs.getInt("seuil");
                double bulkPrice = rs.getDouble("prix_lot");
                discount = new Discount(id, productId, threshold, bulkPrice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return discount;
    }

    /**
     * Ajoute une remise dans la base de données.
     * @param discount remise à ajouter
     * @return true si l'ajout est réussi
     */
    public boolean addDiscount(Discount discount) {
        String sql = "INSERT INTO Reductions(produit_id, seuil, prix_lot) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, discount.getProductId());
            stmt.setInt(2, discount.getThreshold());
            stmt.setDouble(3, discount.getBulkPrice());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour une remise existante dans la base.
     * @param discount remise à mettre à jour
     * @return true si la mise à jour est réussie
     */
    public boolean updateDiscount(Discount discount) {
        String sql = "UPDATE Reductions SET seuil = ?, prix_lot = ? WHERE produit_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, discount.getThreshold());
            stmt.setDouble(2, discount.getBulkPrice());
            stmt.setInt(3, discount.getProductId());
            int affected = stmt.executeUpdate();
            return (affected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime la remise associée à un produit.
     * @param productId identifiant du produit
     * @return true si la suppression est effectuée
     */
    public boolean removeDiscountByProduct(int productId) {
        String sql = "DELETE FROM Reductions WHERE produit_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
