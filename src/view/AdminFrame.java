package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.AdminController;
import model.Admin;
import model.Product;
import model.Discount;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.Map;

/**
 * Interface administrateur pour la gestion des produits, des remises et des statistiques (reporting).
 */
public class AdminFrame extends JFrame {
    private Admin admin;
    private AdminController controller;

    // Composants pour la gestion des produits
    private JList<Product> productList;
    private DefaultListModel<Product> productListModel;
    private JTextField nameField;
    private JTextField brandField;
    private JTextField priceField;
    private JButton addProductButton;
    private JButton updateProductButton;
    private JButton deleteProductButton;

    // Composants pour la gestion des remises
    private JComboBox<Product> productCombo;
    private JTextField thresholdField;
    private JTextField bulkPriceField;
    private JButton savePromoButton;
    private JButton removePromoButton;

    // Panel pour le reporting
    private JPanel statsPanel;

    public AdminFrame(Admin admin, AdminController controller) {
        this.admin = admin;
        this.controller = controller;
        setTitle("Administration - " + admin.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Onglet Produits
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        loadProducts();
        nameField = new JTextField(15);
        brandField = new JTextField(15);
        priceField = new JTextField(10);
        addProductButton = new JButton("Ajouter");
        updateProductButton = new JButton("Modifier");
        deleteProductButton = new JButton("Supprimer");

        JPanel prodFormPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        prodFormPanel.add(new JLabel("Nom:"));
        prodFormPanel.add(nameField);
        prodFormPanel.add(new JLabel("Marque:"));
        prodFormPanel.add(brandField);
        prodFormPanel.add(new JLabel("Prix:"));
        prodFormPanel.add(priceField);

        JPanel prodButtonPanel = new JPanel();
        prodButtonPanel.add(addProductButton);
        prodButtonPanel.add(updateProductButton);
        prodButtonPanel.add(deleteProductButton);

        JPanel prodRightPanel = new JPanel(new BorderLayout());
        prodRightPanel.add(prodFormPanel, BorderLayout.CENTER);
        prodRightPanel.add(prodButtonPanel, BorderLayout.SOUTH);

        JSplitPane prodSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(productList), prodRightPanel);
        prodSplitPane.setDividerLocation(300);
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.add(prodSplitPane, BorderLayout.CENTER);

        // Actions sur les produits
        productList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Product selected = productList.getSelectedValue();
                if (selected != null) {
                    nameField.setText(selected.getName());
                    brandField.setText(selected.getBrand());
                    priceField.setText(String.valueOf(selected.getPrice()));
                }
            }
        });

        addProductButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            double price;
            try {
                price = Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Prix invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Product newProd = new Product(name, brand, price);
            if (controller.addProduct(newProd)) {
                JOptionPane.showMessageDialog(this, "Produit ajouté.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateProductButton.addActionListener(e -> {
            Product selected = productList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un produit.", "Alerte", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            double price;
            try {
                price = Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Prix invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            selected.setName(name);
            selected.setBrand(brand);
            selected.setPrice(price);
            if (controller.updateProduct(selected)) {
                JOptionPane.showMessageDialog(this, "Produit mis à jour.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteProductButton.addActionListener(e -> {
            Product selected = productList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Sélectionnez un produit.", "Alerte", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (controller.deleteProduct(selected.getId())) {
                    JOptionPane.showMessageDialog(this, "Produit supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    loadProducts();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Onglet Promotions
        productCombo = new JComboBox<>();
        for (Product p : controller.getAllProducts()) {
            productCombo.addItem(p);
        }
        thresholdField = new JTextField(5);
        bulkPriceField = new JTextField(5);
        savePromoButton = new JButton("Enregistrer promo");
        removePromoButton = new JButton("Supprimer promo");
        JPanel promoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        promoPanel.add(new JLabel("Produit:"));
        promoPanel.add(productCombo);
        promoPanel.add(new JLabel("Quantité seuil:"));
        promoPanel.add(thresholdField);
        promoPanel.add(new JLabel("Prix en lot:"));
        promoPanel.add(bulkPriceField);
        JPanel promoButtonPanel = new JPanel();
        promoButtonPanel.add(savePromoButton);
        promoButtonPanel.add(removePromoButton);
        JPanel promotionPanel = new JPanel(new BorderLayout());
        promotionPanel.add(promoPanel, BorderLayout.CENTER);
        promotionPanel.add(promoButtonPanel, BorderLayout.SOUTH);

        savePromoButton.addActionListener(e -> {
            Product prod = (Product) productCombo.getSelectedItem();
            if (prod == null) return;
            int threshold;
            double bulkPrice;
            try {
                threshold = Integer.parseInt(thresholdField.getText().trim());
                bulkPrice = Double.parseDouble(bulkPriceField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valeurs invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (controller.saveDiscount(prod.getId(), threshold, bulkPrice)) {
                JOptionPane.showMessageDialog(this, "Promotion enregistrée.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        removePromoButton.addActionListener(e -> {
            Product prod = (Product) productCombo.getSelectedItem();
            if (prod == null) return;
            if (controller.removeDiscount(prod.getId())) {
                JOptionPane.showMessageDialog(this, "Promotion supprimée.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Onglet Statistiques avec JFreeChart
        statsPanel = new JPanel(new GridLayout(2, 1));
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        Map<String, Double> revenueByBrand = controller.getRevenueByBrand();
        revenueByBrand.forEach(pieDataset::setValue);
        JFreeChart pieChart = ChartFactory.createPieChart("Chiffre d'affaires par marque", pieDataset, true, true, false);
        ChartPanel pieChartPanel = new ChartPanel(pieChart);

        DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
        Map<String, Integer> salesByProduct = controller.getSalesQuantityByProduct();
        salesByProduct.forEach((prodName, qty) -> barDataset.addValue(qty, "Ventes", prodName));
        JFreeChart barChart = ChartFactory.createBarChart("Quantité vendue par produit", "Produit", "Quantité", barDataset);
        ChartPanel barChartPanel = new ChartPanel(barChart);

        statsPanel.add(pieChartPanel);
        statsPanel.add(barChartPanel);

        // Création des onglets du back-office
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Produits", productPanel);
        tabbedPane.addTab("Promotions", promotionPanel);
        tabbedPane.addTab("Statistiques", statsPanel);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private void loadProducts() {
        productListModel.clear();
        for (Product p : controller.getAllProducts()) {
            productListModel.addElement(p);
        }
    }
}
