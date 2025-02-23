package org.jj.Menus;

import net.miginfocom.swing.MigLayout;
import org.checkerframework.checker.units.qual.A;
import org.jj.ClientAccount;
import org.jj.Order;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersMenu extends JFrame {

    public OrdersMenu() {
        setTitle("View All Orders");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new MigLayout("insets 20, fill", "[grow]10[right]", "[]20[]40[]"));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("All Orders", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setForeground(Color.WHITE);

        titleLabel.setText("<html><u>All Orders<u><html>");

        JButton backButton = createBackButton();
        titlePanel.add(backButton, BorderLayout.EAST);

        titlePanel.add(titleLabel, BorderLayout.CENTER); // Title aligned to the left
        add(titlePanel, "growx, wrap"); // Allow panel to expand horizontally

        // Add OrderTablePanel directly
        ClientAccount clientAccount = ClientAccount.getInstance();
        List<Order> activeOrders = new ArrayList<>();
        List<Order> settledOrders = new ArrayList<>();
        clientAccount.getOrders().stream().forEach(order -> {
            if (order.quantityFilled() < order.quantity()) {
                activeOrders.add(order);
            } else {
                settledOrders.add(order);
            }
        });

        add(new OrderTablePanel(activeOrders, settledOrders), "grow, wrap"); // TODO: get actual orders

        setVisible(true);
    }


    private JButton createBackButton() {
        JButton button = new JButton("⬅ Back");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(150, 0, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 0), 3));
        button.setPreferredSize(new Dimension(100, 40));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addActionListener(e -> {
            new ClientMain();  // Open previous window
            dispose();  // Close orders window
        });

        return button;
    }
}
