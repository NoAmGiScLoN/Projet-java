package model;

/**
 * Classe abstraite représentant un utilisateur générique dans l'application.
 * Contient les attributs communs à tous les utilisateurs.
 */
public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;

    /**
     * Constructeur complet pour un utilisateur avec identifiant.
     * @param id l'identifiant unique de l'utilisateur
     * @param name le nom de l'utilisateur
     * @param email l'email (utilisé comme identifiant de connexion)
     * @param password le mot de passe de l'utilisateur
     */
    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructeur sans identifiant (lors de la création avant insertion en BDD).
     * @param name le nom de l'utilisateur
     * @param email l'email
     * @param password le mot de passe
     */
    public User(String name, String email, String password) {
        this(0, name, email, password);
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    /**
     * Méthode abstraite pour obtenir le rôle de l'utilisateur.
     * @return une chaîne représentant le rôle (par exemple "client" ou "admin")
     */
    public abstract String getRole();

    @Override
    public String toString() {
        return name + " (" + email + ") - Rôle: " + getRole();
    }
}
