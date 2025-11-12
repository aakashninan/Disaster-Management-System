import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Relief_Camp_Page extends JFrame {

    private JTable campTable;
    private DefaultTableModel model;

    public Relief_Camp_Page() {
        setTitle("Relief Camp Details");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel title = new JLabel("🏕️ Relief Camp Information", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(0, 102, 204));
        add(title, BorderLayout.NORTH);

        // Table model with column names
        model = new DefaultTableModel(new Object[]{"Camp ID", "Location", "Capacity"}, 0);
        campTable = new JTable(model);

        // Styling the table
        campTable.setFont(new Font("Serif", Font.PLAIN, 16));
        campTable.setRowHeight(28);
        campTable.setGridColor(Color.LIGHT_GRAY);
        campTable.setShowGrid(true);

        JTableHeader header = campTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(campTable);
        add(scrollPane, BorderLayout.CENTER);

        // Home Button at the bottom
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

        // Load data from DB
        loadCampData();
    }

    private void loadCampData() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/disaster",
                "root",
                ""
            );

            String sql = "SELECT * FROM camp";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            model.setRowCount(0); // Clear table before loading

            while (rs.next()) {
                String cid = rs.getString("cid");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");

                model.addRow(new Object[]{cid, location, capacity});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading camp data: " + e.getMessage());
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
            new Relief_Camp_Page().setVisible(true);
        });
    }
}


