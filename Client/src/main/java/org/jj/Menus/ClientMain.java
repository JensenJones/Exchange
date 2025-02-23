package org.jj.Menus;

import net.miginfocom.swing.*;
import org.jj.ClientAccount;

import javax.swing.*;
import java.awt.*;

public class ClientMain extends JFrame {
    ClientMain() {
        setTitle("🚀 Jensen's Exchange 🚀");
        setSize(1560, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 30));

        setLayout(new MigLayout("wrap 1", "[center]", "[]40[]80[]"));

        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setOpaque(false);
        JButton btnExit = createExitButton();
        exitPanel.add(btnExit);
        add(exitPanel, "growx");

        JLabel title = new JLabel("🚀 Jensen's Exchange 🚀", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 100));
        title.setForeground(Color.WHITE);
        add(title, "gapbottom 40");

        JPanel buttonPanel = new JPanel(new MigLayout("fill, insets 0", "[grow, center]60[grow, center]60[grow, center]"));

        buttonPanel.setOpaque(false);
        JButton tradeBtn = createLargeButton("Trade 📈");
        JButton pnlBtn = createLargeButton("View PNL 💰");
        JButton ordersBtn = createLargeButton("View Orders 💹");
        buttonPanel.add(tradeBtn, "grow");
        buttonPanel.add(pnlBtn, "grow");
        buttonPanel.add(ordersBtn, "grow");
        add(buttonPanel, "grow");

        tradeBtn.addActionListener(e -> {
            new TradeMenu();
            dispose();
        });

        pnlBtn.addActionListener(e -> {
            new PnlMenu();
            dispose();
        });

        ordersBtn.addActionListener(e -> {
            new OrdersMenu();
            dispose();
        });

        setVisible(true);
    }

    private JButton createLargeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 36));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 10));
        button.setPreferredSize(new Dimension(480, 320));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 80, 80));
                button.repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 50));
                button.repaint();
            }
        });

        return button;
    }

    private JButton createExitButton() {
        Color defaultBackgroundColour = new Color(110, 0, 0);
        JButton button = new JButton("EXIT");
        button.setFont(new Font("Arial", Font.BOLD, 28));
        button.setForeground(Color.WHITE);
        button.setBackground(defaultBackgroundColour);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(150, 0, 0), 4));
        button.setPreferredSize(new Dimension(160, 60));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addActionListener(e -> System.exit(0));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(150, 20, 20));
                button.repaint(); //
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(defaultBackgroundColour);
                button.repaint();
            }
        });

        return button;
    }

    public static void main(String[] args) {
        new ClientAccount();
        SwingUtilities.invokeLater(ClientMain::new);
    }
}
