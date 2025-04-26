package main;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.LoginFrame;

/**
 * Classe principale démarrant l'application Swing avec un Look&Feel moderne.
 */
public class Main {
    public static void main(String[] args) {
        // Applique Flat Light theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Impossible d'initialiser FlatLaf: " + ex.getMessage());
        }
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}