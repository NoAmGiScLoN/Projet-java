package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une commande passée par un client.
 * Une commande regroupe plusieurs lignes de commande (OrderDetail).
 */
public class Order {
    private int id;
    private int userId;
    private Timestamp date;
    private String comment;
    private List<OrderDetail> details;

    /**
     * Constructeur complet.
     * @param id identifiant de la commande
     * @param userId identifiant du client
     * @param date date de la commande
     * @param comment commentaire éventuel
     */
    public Order(int id, int userId, Timestamp date, String comment) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.comment = comment;
        this.details = new ArrayList<>();
    }

    /**
     * Constructeur pour une nouvelle commande.
     * @param userId identifiant du client
     */
    public Order(int userId) {
        this(0, userId, new Timestamp(System.currentTimeMillis()), "");
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public List<OrderDetail> getDetails() { return details; }
    public void setDetails(List<OrderDetail> details) { this.details = details; }

    /**
     * Ajoute un détail à la commande.
     * @param detail un OrderDetail à ajouter
     */
    public void addDetail(OrderDetail detail) {
        details.add(detail);
    }

    /**
     * Calcule le total de la commande en additionnant le sous-total de chaque ligne.
     * @return le montant total de la commande
     */
    public double getTotal() {
        double total = 0;
        for (OrderDetail detail : details) {
            total += detail.getLineTotal();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Commande #" + id + " du " + date;
    }
}
