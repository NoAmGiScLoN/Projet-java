package dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO pour générer des statistiques de vente.
 */
public class StatsDAO {

    /**
     * Calcule le chiffre d'affaires total par marque.
     * @return Map associant la marque au montant total des ventes
     */
    public Map<String, Double> getRevenueByBrand() {
        Map<String, Double> revenueByBrand = new HashMap<>();
        String sql = "SELECT p.marque, SUM(d.quantite * d.prix_unitaire) AS revenue " +
                "FROM DetailsCommande d JOIN Produits p ON d.produit_id = p.id " +
                "GROUP BY p.marque";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String brand = rs.getString("marque");
                double revenue = rs.getDouble("revenue");
                revenueByBrand.put(brand, revenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueByBrand;
    }

    /**
     * Calcule la quantité totale vendue par produit.
     * @return Map associant le nom du produit à la quantité vendue
     */
    public Map<String, Integer> getSalesQuantityByProduct() {
        Map<String, Integer> salesByProduct = new HashMap<>();
        String sql = "SELECT p.nom, SUM(d.quantite) AS totalQty " +
                "FROM DetailsCommande d JOIN Produits p ON d.produit_id = p.id " +
                "GROUP BY p.id, p.nom";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("nom");
                int totalQty = rs.getInt("totalQty");
                salesByProduct.put(name, totalQty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesByProduct;
    }
}
