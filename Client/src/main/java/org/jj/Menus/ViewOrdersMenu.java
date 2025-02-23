package org.jj.Menus;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        JPanel titlePanel = new JPanel(new MigLayout("fillx, insets 0", "[grow]10[right]", "[]"));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Orders:", SwingConstants.LEFT);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        titlePanel.add(title, "align left");

        JButton backButton = createBackButton();
        titlePanel.add(backButton, "align right");

        add(titlePanel, "growx, wrap");

        createOrderTable();

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

    private void createOrderTable() {
        // Main frame setup
        JFrame frame = new JFrame("Menu with Embedded Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Main menu panel (existing menu simulation)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBackground(new Color(50, 50, 50)); // Simulate current background

        // Title label
        JLabel titleLabel = new JLabel("Orders Table", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Embed JTable into the menu
        String[] columns = {"ID", "Name", "Age", "Country"};
        Object[][] data = {
                {1, "Alice", 24, "USA"},
                {2, "Bob", 30, "UK"},
                {3, "Charlie", 28, "Canada"},
                {4, "David", 35, "Germany"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);

        // Table customization to blend with menu
        table.setBackground(new Color(70, 70, 70));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.DARK_GRAY);
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);

        // Set header style
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 30, 30));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        // Scroll pane with no border (for seamless look)
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Add components to menu panel
        menuPanel.add(titleLabel, BorderLayout.NORTH);
        menuPanel.add(scrollPane, BorderLayout.CENTER);

        // Embed menu panel into frame
        frame.add(menuPanel);
        frame.setVisible(true);
    }
}
