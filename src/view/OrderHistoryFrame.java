package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Client;
import model.Order;

/**
 * Fenêtre affichant l'historique des commandes d'un client.
 */
public class OrderHistoryFrame extends JFrame {
    public OrderHistoryFrame(Client client, List<Order> orders) {
        setTitle("Historique des commandes de " + client.getName());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append("Commande #" + order.getId() + " du " + order.getDate() + ":\n");
            order.getDetails().forEach(detail -> sb.append("  - " + detail.toString() + "\n"));
            sb.append("Total : " + String.format("%.2f", order.getTotal()) + " €\n\n");
        }
        textArea.setText(sb.toString());
        add(new JScrollPane(textArea));
    }
}
