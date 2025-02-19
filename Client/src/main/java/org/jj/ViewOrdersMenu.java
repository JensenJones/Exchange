package org.jj;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;


public class ViewOrdersMenu extends JFrame {

    public ViewOrdersMenu() {

        setTitle("View All Orders");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new MigLayout("insets 20", "[grow]10[right]", "[]20[]40[]"));

        // Show active orders
        JPanel activeOrdersTitlePanel = new JPanel(new MigLayout("fillx, insets 0", "[grow]10[right]", "[]"));
        activeOrdersTitlePanel.setOpaque(false);

        JLabel activeOrdersLabel = new JLabel("Active Orders:", SwingConstants.LEFT);
        activeOrdersLabel.setFont(new Font("Arial", Font.BOLD, 40));
        activeOrdersLabel.setForeground(Color.WHITE);
        activeOrdersTitlePanel.add(activeOrdersLabel, "align left");

        JButton backButton = createBackButton();
        activeOrdersTitlePanel.add(backButton, "align right");

        add(activeOrdersTitlePanel, "growx, wrap");


        // Show settled orders
        JPanel settledOrdersActivePanel = new JPanel(new MigLayout("fillx, insets 0", "[grow]10[right]", "[]"));
        settledOrdersActivePanel.setOpaque(false);

        JLabel settledOrdersLabel = new JLabel("Settled Orders:", SwingConstants.LEFT);
        settledOrdersLabel.setFont(new Font("Arial", Font.BOLD, 40));
        settledOrdersLabel.setForeground(Color.WHITE);
        settledOrdersActivePanel.add(settledOrdersLabel, "align left");

        add(settledOrdersActivePanel, "growx, wrap");


        setVisible(true);
    }

    private JButton createBackButton() {
        JButton button = new JButton("⬅ Back");
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(150, 0, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 0), 3));
        button.setPreferredSize(new Dimension(100, 40));  // 🔹 Made smaller
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addActionListener(e -> {
            new ClientMain();  // Open previous window
            dispose();  // Close deposit window
        });

        return button;
    }
}
