package org.jj;

import net.miginfocom.swing.MigLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class DepositMenu extends JFrame {
    private double balance;
    private JLabel balanceLabel;
    private JTextField depositField;

    public DepositMenu(double balance) {
        this.balance = balance;

        setTitle("💰 Deposit Funds");
        setSize(500, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new MigLayout("insets 20", "[grow]10[right]", "[]20[]40[]"));

        // Title + Back Button Panel (Back Button Right-Aligned)
        JPanel titlePanel = new JPanel(new MigLayout("fillx, insets 0", "[grow]10[right]", "[]"));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Deposit Menu 💳", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, "align left");

        JButton backButton = createBackButton();
        titlePanel.add(backButton, "align right");

        add(titlePanel, "growx, wrap");

        // Deposit Input + Button Panel
        JPanel depositPanel = new JPanel(new MigLayout("insets 0", "[grow]10[shrink]"));
        depositPanel.setOpaque(false);

        depositField = new JTextField("Deposit amount", 10);
        depositField.setFont(new Font("Arial", Font.PLAIN, 20));
        depositField.setForeground(Color.GRAY);
        depositField.setBackground(new Color(50, 50, 50));
        depositField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        depositField.setHorizontalAlignment(JTextField.CENTER);

        depositField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (depositField.getText().equals("Deposit amount")) {
                    depositField.setText("");
                    depositField.setForeground(Color.WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if (depositField.getText().isEmpty()) {
                    depositField.setText("Deposit amount");
                    depositField.setForeground(Color.GRAY);
                }
            }
        });

        JButton depositButton = createDepositButton();

        depositPanel.add(depositField, "growx");
        depositPanel.add(depositButton, "shrink");
        add(depositPanel, "growx, wrap");

        // Balance Display (Bottom Left)
        balanceLabel = new JLabel("Current Balance: $" + balance);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(Color.WHITE);
        add(balanceLabel, "align left");

        setVisible(true);
    }

    private @NotNull JButton createDepositButton() {
        JButton depositButton = new JButton("Deposit");
        depositButton.setFont(new Font("Arial", Font.BOLD, 24));
        depositButton.setForeground(Color.WHITE);
        depositButton.setBackground(new Color(0, 150, 0));
        depositButton.setFocusPainted(false);
        depositButton.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 0), 5));
        depositButton.setPreferredSize(new Dimension(140, 50));  // 🔹 Adjusted button size
        depositButton.setOpaque(true);
        depositButton.setContentAreaFilled(true);

        depositButton.addActionListener(e -> handleDeposit());
        return depositButton;
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
            new ClientMain(balance);  // Open previous window
            dispose();  // Close deposit window
        });

        return button;
    }

    private void handleDeposit() {
        try {
            double depositAmount = Double.parseDouble(depositField.getText());
            if (depositAmount <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            balance += depositAmount;
            balanceLabel.setText("Current Balance: $" + balance);
            depositField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
