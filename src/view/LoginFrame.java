package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.ShopController;
import dao.UserDAO;
import model.Client;
import model.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Version simple et robuste de la page de login pour Watchers.
 * Charge le logo, affiche email/mot de passe, et ouvre StoreFrame après connexion.
 */
public class LoginFrame extends JFrame {
    private final JTextField emailField  = new JTextField(20);
    private final JPasswordField pwdField = new JPasswordField(20);
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        super("Connexion – Watchers");
        // Appliquer FlatLaf avant de construire la fenêtre
        try { UIManager.setLookAndFeel(new FlatLightLaf()); }
        catch (Exception ignored) {}
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Conteneur principal en BorderLayout
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        // === HEADER : logo centré ===
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try (InputStream is = getClass().getResourceAsStream("/assets/logo.png")) {
            if (is != null) {
                BufferedImage img = ImageIO.read(is);
                Image scaled = img.getScaledInstance(120, -1, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) {
            System.err.println("Impossible de charger logo.png");
        }
        root.add(logoLabel, BorderLayout.NORTH);

        // === CENTER : formulaire email / mot de passe ===
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Adresse mail :"), gbc);
        gbc.gridx = 1;
        form.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        form.add(pwdField, gbc);

        root.add(form, BorderLayout.CENTER);

        // === FOOTER : boutons ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setBackground(Color.WHITE);

        JButton registerBtn = new JButton("S'inscrire");
        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
        footer.add(registerBtn);

        JButton loginBtn = new JButton("Se connecter");
        loginBtn.setBackground(new Color(0x1976D2));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.addActionListener(e -> onLogin());
        footer.add(loginBtn);

        root.add(footer, BorderLayout.SOUTH);

        pack();
    }

    private void onLogin() {
        String email = emailField.getText().trim();
        String pwd   = new String(pwdField.getPassword());
        if (email.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires.",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.login(email, pwd);
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Email ou mot de passe incorrect.",
                    "Erreur d'authentification",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connexion réussie → ouvre la boutique
        dispose();
        new StoreFrame((Client) user, new ShopController((Client) user))
                .setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
