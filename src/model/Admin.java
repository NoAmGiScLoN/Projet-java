package model;

/**
 * Classe représentant un administrateur de l'application.
 */
public class Admin extends User {
    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    public Admin(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public String getRole() {
        return "admin";
    }
}

