import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Reports_Updates_Page extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public Reports_Updates_Page() {
        setTitle("Reports Updates Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel title = new JLabel("📰 Recent Reports & Updates", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(0, 102, 204));
        add(title, BorderLayout.NORTH);

        // Table model with column names
        model = new DefaultTableModel(new Object[]{"ID", "Incident (Important News)", "Location"}, 0);
        table = new JTable(model);

        // Custom cell renderer to highlight incident column in red
        DefaultTableCellRenderer redRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1) { // Incident column
                    cell.setForeground(Color.RED);
                    cell.setFont(cell.getFont().deriveFont(Font.BOLD));
                } else {
                    cell.setForeground(Color.BLACK);
                }
                return cell;
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(redRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Home Button
        JButton homeBtn = new JButton("🏠 Home");
        homeBtn.setBackground(new Color(0, 102, 204));
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        homeBtn.setFocusPainted(false);
        add(homeBtn, BorderLayout.SOUTH);

        // Home button action
        homeBtn.addActionListener(e -> {
            dispose(); // close this page
            new Home_Page().setVisible(true); // open Home Page
        });

        // Load data
        loadRecentReports();
    }

    private void loadRecentReports() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/disaster", "root", "");
            String sql = "SELECT * FROM reports ORDER BY iid DESC LIMIT 5";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            model.setRowCount(0); // Clear old data

            while (rs.next()) {
                String id = rs.getString("iid");
                String incident = rs.getString("incident");
                String location = rs.getString("location");
                model.addRow(new Object[]{id, incident, location});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Run the JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Reports_Updates_Page().setVisible(true);
        });
    }
}

