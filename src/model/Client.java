package model;

/**
 * Classe représentant un client de l'application.
 */
public class Client extends User {
    /**
     * Constructeur complet.
     */
    public Client(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    /**
     * Constructeur sans identifiant.
     */
    public Client(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public String getRole() {
        return "client";
    }
}
