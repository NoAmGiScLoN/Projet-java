package model;

/**
 * Classe représentant un produit du catalogue.
 */
public class Product {
    private int id;
    private String name;
    private String brand;
    private double price;        // prix unitaire standard
    private Discount discount;   // remise applicable (null si aucune)

    public Product(int id, String name, String brand, double price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public Product(String name, String brand, double price) {
        this(0, name, brand, price);
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

    /**
     * Calcule le prix total pour une quantité donnée en tenant compte d'une éventuelle remise.
     * Si une remise est définie et que la quantité est supérieure ou égale au seuil,
     * le prix en vrac est appliqué pour le maximum de lots, puis le reste au prix unitaire.
     * @param quantity la quantité achetée
     * @return le prix total
     */
    public double calculateTotalPrice(int quantity) {
        if (discount != null && quantity >= discount.getThreshold()) {
            int lotSize = discount.getThreshold();
            double lotPrice = discount.getBulkPrice();
            int numLots = quantity / lotSize;
            int remainder = quantity % lotSize;
            return numLots * lotPrice + remainder * price;
        } else {
            return quantity * price;
        }
    }

    @Override
    public String toString() {
        return name + " (" + brand + ") - " + price + " €" + (discount != null ? " [Promo]" : "");
    }
}
