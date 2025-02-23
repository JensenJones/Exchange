package org.jj.Menus;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PnlMenu extends JFrame {

    public PnlMenu() {
        setTitle("Profit & Loss 📊");
        setSize(500, 320); // Reduced size for a cleaner look
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new MigLayout("insets 20", "[grow][shrink]", "[]20[]40[]"));

        // Title + Back Button in Same Line
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Profit & Loss", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.WEST);

        JButton backButton = createBackButton();
        titlePanel.add(backButton, BorderLayout.EAST);

        add(titlePanel, "growx, span, wrap");

        // PNL Display (Inside a Box)
        JPanel pnlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlPanel.setBackground(new Color(50, 50, 50)); // Slightly lighter gray box
        pnlPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 3)); // Subtle border
        pnlPanel.setPreferredSize(new Dimension(400, 100));


        setVisible(true);
    }

    private JButton createBackButton() {
        JButton button = new JButton("⬅ Back");
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(150, 0, 0));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 0, 0), 2));
        button.setPreferredSize(new Dimension(100, 35));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addActionListener(e -> {
            new ClientMain();  // Open previous window
            dispose();  // Close PNL window
        });

        return button;
    }
}
