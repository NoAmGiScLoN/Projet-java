package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import controller.ShopController;
import model.Client;
import model.Product;

/**
 * Interface principale client pour Watchers - Boutique de Montres.
 */
public class StoreFrame extends JFrame {
    private ShopController controller;
    private Client client;

    public StoreFrame(Client client, ShopController controller) {
        super("Watchers - Boutique de Montres");
        this.client = client;
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);
        add(createProductGrid(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBorder(new EmptyBorder(10, 20, 10, 20));
        header.setBackground(Color.WHITE);

        // Logo Watchers
        JLabel logo = new JLabel(new ImageIcon(getClass().getResource("/assets/logo.png")));
        header.add(logo, BorderLayout.WEST);

        // Barre de recherche pour montres
        JTextField search = new JTextField();
        search.setPreferredSize(new Dimension(400, 30));
        search.setToolTipText("Rechercher des montres, marques...");
        search.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        header.add(search, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttons.setOpaque(false);
        buttons.add(new JButton("🛒 Panier"));
        buttons.add(new JButton("👤 Compte"));
        header.add(buttons, BorderLayout.EAST);

        return header;
    }

    private JScrollPane createSidebar() {
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Toutes montres");
        model.addElement("Hommes");
        model.addElement("Femmes");
        model.addElement("Automatique");
        model.addElement("Connectée");
        model.addElement("Plongeuse");
        model.addElement("Vintage");
        JList<String> list = new JList<>(model);
        list.setBorder(new EmptyBorder(10,10,10,10));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellWidth(180);
        return new JScrollPane(list);
    }

    private JScrollPane createProductGrid() {
        List<Product> products = controller.getAllProducts();
        JPanel grid = new JPanel(new GridLayout(0, 4, 20, 20));
        grid.setBorder(new EmptyBorder(20,20,20,20));

        for (Product p : products) {
            grid.add(createProductCard(p));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10,10,10,10)
        ));
        card.setBackground(Color.WHITE);

        JLabel img = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/products/" + p.getId() + ".png"));
        img.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
        card.add(img, BorderLayout.NORTH);

        JLabel name = new JLabel(p.getName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setBorder(new EmptyBorder(10,0,5,0));

        JLabel price = new JLabel(String.format("€ %.2f", p.getPrice()));
        price.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        price.setForeground(new Color(0xE53935));

        JButton add = new JButton("Ajouter");
        add.setBackground(new Color(0x1976D2));
        add.setForeground(Color.WHITE);
        add.setFocusPainted(false);
        add.addActionListener(e -> controller.addToCart(p, 1));

        JPanel info = new JPanel(new GridLayout(0,1,0,5));
        info.setOpaque(false);
        info.add(name);
        info.add(price);
        info.add(add);

        card.add(info, BorderLayout.CENTER);
        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBorder(new EmptyBorder(10,0,10,0));
        footer.add(new JLabel("© 2025 Watchers - Tous droits réservés"));
        return footer;
    }
}
