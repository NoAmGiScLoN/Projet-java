package controller;

import java.util.List;
import java.util.Map;
import model.Admin;
import model.Product;
import model.Discount;
import dao.ProductDAO;
import dao.DiscountDAO;
import dao.StatsDAO;

/**
 * Contrôleur dédié aux opérations administrateur.
 */
public class AdminController {
    private Admin admin;
    private ProductDAO productDAO;
    private DiscountDAO discountDAO;
    private StatsDAO statsDAO;

    public AdminController(Admin admin) {
        this.admin = admin;
        this.productDAO = new ProductDAO();
        this.discountDAO = new DiscountDAO();
        this.statsDAO = new StatsDAO();
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    public boolean deleteProduct(int productId) {
        return productDAO.deleteProduct(productId);
    }

    public Discount getDiscountByProduct(int productId) {
        return discountDAO.getDiscountByProductId(productId);
    }

    /**
     * Ajoute ou met à jour la remise pour un produit.
     * @param productId identifiant du produit
     * @param threshold quantité minimale pour la remise
     * @param bulkPrice prix en lot
     * @return true en cas de succès
     */
    public boolean saveDiscount(int productId, int threshold, double bulkPrice) {
        Discount existing = discountDAO.getDiscountByProductId(productId);
        if (existing != null) {
            existing.setThreshold(threshold);
            existing.setBulkPrice(bulkPrice);
            return discountDAO.updateDiscount(existing);
        } else {
            Discount newDiscount = new Discount(productId, threshold, bulkPrice);
            return discountDAO.addDiscount(newDiscount);
        }
    }

    public boolean removeDiscount(int productId) {
        return discountDAO.removeDiscountByProduct(productId);
    }

    public Map<String, Double> getRevenueByBrand() {
        return statsDAO.getRevenueByBrand();
    }

    public Map<String, Integer> getSalesQuantityByProduct() {
        return statsDAO.getSalesQuantityByProduct();
    }
}
