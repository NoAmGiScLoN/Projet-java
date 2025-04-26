package model;

/**
 * Classe représentant le détail d'une commande (une ligne d'achat).
 */
public class OrderDetail {
    private int id;
    private int orderId;
    private int productId;
    private Product product; // pour affichage
    private int quantity;
    private double unitPrice;

    /**
     * Constructeur complet.
     * @param id identifiant du détail
     * @param orderId identifiant de la commande
     * @param productId identifiant du produit
     * @param quantity quantité achetée
     * @param unitPrice prix unitaire appliqué
     */
    public OrderDetail(int id, int orderId, int productId, int quantity, double unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /**
     * Constructeur sans identifiant.
     * @param orderId identifiant de la commande
     * @param productId identifiant du produit
     * @param quantity quantité achetée
     * @param unitPrice prix unitaire appliqué
     */
    public OrderDetail(int orderId, int productId, int quantity, double unitPrice) {
        this(0, orderId, productId, quantity, unitPrice);
    }

    /**
     * Constructeur pratique avec objet produit.
     * @param product objet produit
     * @param quantity quantité achetée
     * @param unitPrice prix unitaire appliqué
     */
    public OrderDetail(Product product, int quantity, double unitPrice) {
        this(0, 0, product.getId(), quantity, unitPrice);
        this.product = product;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    /**
     * Calcule le sous-total de cette ligne (prix unitaire * quantité).
     * @return montant total de la ligne
     */
    public double getLineTotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        String prodName = (product != null ? product.getName() : "Produit #" + productId);
        return prodName + " x" + quantity + " = " + String.format("%.2f", getLineTotal()) + " €";
    }
}
