package model;

/**
 * Classe représentant une remise pour un produit.
 * Par exemple, "10 pour 4€" signifie que si l'on achète 10 unités,
 * le lot est facturé à 4€ (ce qui revient à un prix unitaire réduit).
 */
public class Discount {
    private int id;
    private int productId;
    private int threshold;       // Quantité minimale pour bénéficier de la remise
    private double bulkPrice;    // Prix à appliquer pour le lot de 'threshold' unités

    /**
     * Constructeur complet.
     * @param id l'identifiant de la remise
     * @param productId l'identifiant du produit concerné
     * @param threshold la quantité minimale pour la remise
     * @param bulkPrice le prix du lot
     */
    public Discount(int id, int productId, int threshold, double bulkPrice) {
        this.id = id;
        this.productId = productId;
        this.threshold = threshold;
        this.bulkPrice = bulkPrice;
    }

    /**
     * Constructeur sans identifiant (pour insertion en base).
     * @param productId l'identifiant du produit concerné
     * @param threshold la quantité minimale pour la remise
     * @param bulkPrice le prix du lot
     */
    public Discount(int productId, int threshold, double bulkPrice) {
        this(0, productId, threshold, bulkPrice);
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }

    public double getBulkPrice() { return bulkPrice; }
    public void setBulkPrice(double bulkPrice) { this.bulkPrice = bulkPrice; }

    @Override
    public String toString() {
        return "Promotion: " + threshold + " pour " + bulkPrice + " €";
    }
}
