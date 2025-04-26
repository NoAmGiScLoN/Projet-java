package controller;

import java.util.List;
import model.Client;
import model.Order;
import model.OrderDetail;
import model.Product;
import dao.OrderDAO;
import dao.ProductDAO;

/**
 * Contrôleur dédié aux opérations du client.
 */
public class ShopController {
    private Client client;
    private Order currentOrder;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;

    /**
     * Constructeur initialisant le client et un nouveau panier.
     * @param client le client connecté
     */
    public ShopController(Client client) {
        this.client = client;
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
        this.currentOrder = new Order(client.getId());
    }

    /**
     * Retourne la liste de tous les produits du catalogue.
     * @return liste des produits
     */
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    /**
     * Ajoute un produit au panier.
     * Si le produit est déjà présent, met à jour la quantité et recalcul le prix unitaire.
     * @param product produit sélectionné
     * @param quantity quantité à ajouter
     */
    public void addToCart(Product product, int quantity) {
        OrderDetail existing = null;
        for (OrderDetail detail : currentOrder.getDetails()) {
            if (detail.getProductId() == product.getId()) {
                existing = detail;
                break;
            }
        }
        if (existing != null) {
            int newQuantity = existing.getQuantity() + quantity;
            existing.setQuantity(newQuantity);
            double newUnitPrice = product.calculateTotalPrice(newQuantity) / newQuantity;
            existing.setUnitPrice(newUnitPrice);
        } else {
            double unitPrice = product.getDiscount() != null && quantity >= product.getDiscount().getThreshold()
                    ? product.getDiscount().getBulkPrice()
                    : product.getPrice();
            OrderDetail detail = new OrderDetail(product, quantity, unitPrice);
            currentOrder.addDetail(detail);
        }
    }

    /**
     * Retire un produit du panier.
     * @param productId identifiant du produit à retirer
     */
    public void removeFromCart(int productId) {
        currentOrder.getDetails().removeIf(detail -> detail.getProductId() == productId);
    }

    /**
     * Valide le panier et enregistre la commande en base.
     * @return le total de la commande ou -1 en cas d'erreur
     */
    public double placeOrder() {
        if (currentOrder.getDetails().isEmpty()) {
            return -1;
        }
        currentOrder.setDate(new java.sql.Timestamp(System.currentTimeMillis()));
        boolean success = orderDAO.createOrder(currentOrder);
        if (success) {
            double total = currentOrder.getTotal();
            currentOrder = new Order(client.getId());
            return total;
        } else {
            return -1;
        }
    }

    /**
     * Récupère l'historique des commandes du client.
     * @return liste des commandes
     */
    public List<Order> getOrderHistory() {
        return orderDAO.getOrdersByUser(client.getId());
    }

    public Client getClient() { return client; }
    public List<OrderDetail> getCartDetails() { return currentOrder.getDetails(); }
}
