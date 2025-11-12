import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Resource_Management_page extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtItemId, txtName, txtQuantity, txtLocation, txtSearchName, txtSearchLocation;
    private JTable table;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Akku703499@";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Resource_Management_page frame = new Resource_Management_page();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Resource_Management_page() {
        setTitle("Resource Management Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 520);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Resource Management");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(250, 10, 300, 30);
        contentPane.add(lblTitle);

        JLabel lblItemId = new JLabel("Item ID:");
        lblItemId.setBounds(30, 60, 80, 20);
        contentPane.add(lblItemId);

        txtItemId = new JTextField();
        txtItemId.setBounds(120, 60, 150, 25);
        contentPane.add(txtItemId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 100, 80, 20);
        contentPane.add(lblName);

        txtName = new JTextField();
        txtName.setBounds(120, 100, 150, 25);
        contentPane.add(txtName);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(30, 140, 80, 20);
        contentPane.add(lblQuantity);

        txtQuantity = new JTextField();
        txtQuantity.setBounds(120, 140, 150, 25);
        contentPane.add(txtQuantity);

        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setBounds(30, 180, 80, 20);
        contentPane.add(lblLocation);

        txtLocation = new JTextField();
        txtLocation.setBounds(120, 180, 150, 25);
        contentPane.add(txtLocation);

        JButton btnAdd = new JButton("Add Resource");
        btnAdd.setBounds(120, 220, 150, 30);
        btnAdd.addActionListener(e -> addResource());
        contentPane.add(btnAdd);

        // 🔎 Search Section
        JLabel lblSearch = new JLabel("Search Resource");
        lblSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSearch.setBounds(400, 60, 200, 25);
        contentPane.add(lblSearch);

        JLabel lblSearchName = new JLabel("Name:");
        lblSearchName.setBounds(350, 100, 80, 20);
        contentPane.add(lblSearchName);

        txtSearchName = new JTextField();
        txtSearchName.setBounds(420, 100, 150, 25);
        contentPane.add(txtSearchName);

        JLabel lblSearchLocation = new JLabel("Location:");
        lblSearchLocation.setBounds(350, 140, 80, 20);
        contentPane.add(lblSearchLocation);

        txtSearchLocation = new JTextField();
        txtSearchLocation.setBounds(420, 140, 150, 25);
        contentPane.add(txtSearchLocation);

        JButton btnSearch = new JButton("Search");
        btnSearch.setBounds(420, 180, 150, 30);
        btnSearch.addActionListener(e -> searchResource());
        contentPane.add(btnSearch);

        // Table to display search results
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 270, 670, 150);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        // 🏠 Go Home Button
        JButton btnHome = new JButton("🏠 Go Home");
        btnHome.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnHome.setForeground(Color.BLACK); // black text
        btnHome.setBounds(300, 430, 150, 30);
        contentPane.add(btnHome);

        // Action for home
        btnHome.addActionListener(e -> {
            dispose(); // close this page
            new Home_Page().setVisible(true); // open Home_Page (make sure it's implemented)
        });
    }

    private void addResource() {
        String itemId = txtItemId.getText();
        String name = txtName.getText();
        String quantity = txtQuantity.getText();
        String location = txtLocation.getText();

        if (itemId.isEmpty() || name.isEmpty() || quantity.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "INSERT INTO resource (item_id, name, quantity, location) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, itemId);
            pst.setString(2, name);
            pst.setInt(3, Integer.parseInt(quantity));
            pst.setString(4, location);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Resource added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void searchResource() {
        String name = txtSearchName.getText();
        String location = txtSearchLocation.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT * FROM resource WHERE name LIKE ? AND location LIKE ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, "%" + name + "%");
            pst.setString(2, "%" + location + "%");

            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Item ID", "Name", "Quantity", "Location"}, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("item_id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getString("location")
                });
            }

            table.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

