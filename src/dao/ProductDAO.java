package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Discount;
import model.Product;

/**
 * DAO pour l'accès aux produits.
 */
public class ProductDAO {

    /**
     * Récupère la liste complète de tous les produits, incluant leur remise éventuelle.
     * @return liste des produits
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.nom, p.marque, p.prix, r.id AS rid, r.seuil, r.prix_lot " +
                "FROM Produits p LEFT JOIN Reductions r ON p.id = r.produit_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("nom");
                String brand = rs.getString("marque");
                double price = rs.getDouble("prix");
                Product product = new Product(id, name, brand, price);
                int rid = rs.getInt("rid");
                if (!rs.wasNull()) {
                    int threshold = rs.getInt("seuil");
                    double bulkPrice = rs.getDouble("prix_lot");
                    Discount discount = new Discount(rid, id, threshold, bulkPrice);
                    product.setDiscount(discount);
                }
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Insère un nouveau produit dans la base.
     * Si le produit possède une remise, l'insère également en appelant DiscountDAO.
     * @param product le produit à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO Produits(nom, marque, prix) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getBrand());
            stmt.setDouble(3, product.getPrice());
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                return false;
            }
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int newId = keys.getInt(1);
                product.setId(newId);
                if (product.getDiscount() != null) {
                    Discount disc = product.getDiscount();
                    disc.setProductId(newId);
                    DiscountDAO discDAO = new DiscountDAO();
                    discDAO.addDiscount(disc);
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour un produit existant.
     * @param product le produit modifié
     * @return true si la mise à jour est réussie, false sinon
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Produits SET nom = ?, marque = ?, prix = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getBrand());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getId());
            int affected = stmt.executeUpdate();
            return (affected > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un produit de la base, ainsi que sa remise associée le cas échéant.
     * @param productId identifiant du produit à supprimer
     * @return true si la suppression est effectuée, false sinon
     */
    public boolean deleteProduct(int productId) {
        try {
            // Supprimer d'abord la remise si elle existe
            DiscountDAO discDAO = new DiscountDAO();
            discDAO.removeDiscountByProduct(productId);
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement("DELETE FROM Produits WHERE id = ?")) {
                stmt.setInt(1, productId);
                int affected = stmt.executeUpdate();
                return (affected > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
