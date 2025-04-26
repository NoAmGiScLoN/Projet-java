package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.ShopController;
import dao.UserDAO;
import model.Client;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Fenêtre d'inscription repensée pour la boutique Watchers.
 * Applique le même style que StoreFrame et ouvre directement StoreFrame après inscription.
 */
public class RegisterFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerBtn;
    private UserDAO userDAO;

    public RegisterFrame() {
        super("Inscription - Watchers");
        // Assure-toi que FlatLaf est déjà appliqué dans Main
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header (logo + titre) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel(new ImageIcon(getClass().getResource("/assets/logo.png")));
        header.add(logo, BorderLayout.WEST);

        JLabel title = new JLabel("Créez votre compte Watchers");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // --- Formulaire ---
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Nom complet :"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField();
        form.add(nameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField();
        form.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        form.add(passwordField, gbc);

        add(form, BorderLayout.CENTER);

        // --- Pied de page (bouton + retour) ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(0, 20, 10, 20));

        JButton backBtn = new JButton("← Retour");
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        footer.add(backBtn);

        registerBtn = new JButton("S'inscrire");
        registerBtn.setBackground(new Color(0x1976D2));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.addActionListener(e -> onRegister());
        footer.add(registerBtn);

        add(footer, BorderLayout.SOUTH);

        userDAO = new UserDAO();
    }

    private void onRegister() {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pwd   = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires.",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = userDAO.register(name, email, pwd);
        if (!ok) {
            JOptionPane.showMessageDialog(this,
                    "Cet email est déjà utilisé.",
                    "Inscription échouée",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Inscription réussie → connexion automatique
        User user = userDAO.login(email, pwd);
        if (user instanceof Client) {
            JOptionPane.showMessageDialog(this,
                    "Bienvenue, " + name + " !",
                    "Inscription réussie",
                    JOptionPane.INFORMATION_MESSAGE);

            ShopController shopCtrl = new ShopController((Client) user);
            new StoreFrame((Client) user, shopCtrl).setVisible(true);
            dispose();
        } else {
            // cas improbable, ou erreur de rôle
            JOptionPane.showMessageDialog(this,
                    "Inscription réussie, merci de vous reconnecter.",
                    "OK",
                    JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
