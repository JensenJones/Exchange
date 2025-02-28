package org.jj.Menus;

import org.jj.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderTablePanel extends JPanel {

    // Define dark theme colors
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color TABLE_HEADER_COLOR = new Color(35, 35, 35);
    private static final Color TABLE_ROW_COLOR = new Color(45, 45, 45);

    public OrderTablePanel(List<Order> activeOrders, List<Order> pastOrders) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BACKGROUND_COLOR);

        add(createSection("Active Orders", activeOrders));
        add(Box.createVerticalStrut(10)); // Space between tables
        add(createSection("Settled Orders", pastOrders));
    }

    private JPanel createSection(String title, List<Order> orders) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);

        JTable table = createOrderTable(orders);
        JScrollPane scrollPane = new JScrollPane(table);

        // Dark scroll pane
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JTable createOrderTable(List<Order> orders) {
        String[] columnNames = {"Order ID", "Product", "Price", "Quantity", "Quantity Filled", "Expiry Type", "Buy/Sell"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Order order : orders) {
            model.addRow(new Object[]{
                    order.orderId(),
                    order.product(),
                    order.price(),
                    order.quantity(),
                    order.quantityFilled(),
                    order.expiry(),
                    order.buySell()
            });
        }

        JTable table = new JTable(model);
        table.setEnabled(false);

        // Dark theme for table
        table.setBackground(TABLE_ROW_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(Color.DARK_GRAY);

        // Header customization
        table.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        // Row height
        table.setRowHeight(30);

        // Align text center in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Custom renderer for Buy/Sell column
        DefaultTableCellRenderer buySellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String text = value.toString();

                if (text.equalsIgnoreCase("Buy")) {
                    cell.setForeground(Color.GREEN);
                } else if (text.equalsIgnoreCase("Sell")) {
                    cell.setForeground(Color.RED);
                } else {
                    cell.setForeground(TEXT_COLOR);
                }

                return cell;
            }
        };

        // Apply custom renderer to the Buy/Sell column (index 6)
        table.getColumnModel().getColumn(6).setCellRenderer(buySellRenderer);

        return table;
    }

    public static void createAndShowGUI(List<Order> activeOrders, List<Order> pastOrders) {
        JFrame frame = new JFrame("Order Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.add(new OrderTablePanel(activeOrders, pastOrders));
        frame.setVisible(true);
    }
}
