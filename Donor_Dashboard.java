import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Donor_Dashboard extends JFrame {

    private JTextField nameField, amountField;
    private JLabel totalLabel;

    public Donor_Dashboard() {
        setTitle("Donor Dashboard - Disaster Relief Fund");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 248, 250)); // Neutral light background

        // Title Banner
        JLabel title = new JLabel("Disaster Relief Donation Portal", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 22));
        title.setForeground(new Color(30, 60, 120)); // Dark formal blue
        title.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(title, BorderLayout.NORTH);

        // Input Panel (Card Style)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true),
                "Donation Details",
                0, 0, new Font("SansSerif", Font.BOLD, 14), new Color(25, 25, 112)
        ));
        inputPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Donor Name:");
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 0;
        inputPanel.add(nameField, gbc);

        JLabel amountLabel = new JLabel("Donation Amount (₹):");
        amountLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(amountLabel, gbc);

        amountField = new JTextField(20);
        amountField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        inputPanel.add(amountField, gbc);

        JButton donateBtn = new JButton("Donate Now");
        styleButton(donateBtn, Color.WHITE, Color.BLACK); // White bg, Black text
        gbc.gridwidth = 2; gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(donateBtn, gbc);

        add(inputPanel, BorderLayout.CENTER);

        // Footer Panel (Total + Home Button)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(245, 248, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        totalLabel = new JLabel("Total Collected: ₹0", JLabel.CENTER);
        totalLabel.setFont(new Font("Serif", Font.BOLD, 16));
        totalLabel.setForeground(new Color(139, 0, 0));
        footerPanel.add(totalLabel, BorderLayout.CENTER);

        // Go Home Button with emoji + black text
        JButton homeBtn = new JButton("🏠 Go Home");
        styleButton(homeBtn, new Color(200, 200, 200), Color.BLACK); // Gray bg, Black text
        footerPanel.add(homeBtn, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);

        // Button action
        donateBtn.addActionListener(e -> insertDonation());

        // Home button action
        homeBtn.addActionListener(e -> {
            dispose();
            new Home_Page().setVisible(true);
        });

        // Load total when starting
        updateTotalAmount();
    }

    // Button Styling Method (supports custom text color)
    private void styleButton(JButton btn, Color bgColor, Color textColor) {
        btn.setBackground(bgColor);
        btn.setForeground(textColor);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)); // subtle border
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    // Insert donation into DB
    private void insertDonation() {
        String name = nameField.getText().trim();
        String amtStr = amountField.getText().trim();

        if (name.isEmpty() || amtStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            int amount = Integer.parseInt(amtStr);

            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/disaster", "root", ""
            );
            String sql = "INSERT INTO accounts(name, amount) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, amount);
            ps.executeUpdate();

            ps.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Thank you for your contribution, " + name + "!");
            nameField.setText("");
            amountField.setText("");

            updateTotalAmount();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for amount.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Calculate total donations
    private void updateTotalAmount() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/disaster", "root", "Akku703499@"
            );
            String sql = "SELECT SUM(amount) AS total FROM accounts";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                totalLabel.setText("Total Collected: ₹" + total);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            totalLabel.setText("Error fetching total.");
        }
    }

    // Run JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Donor_Dashboard().setVisible(true);
        });
    }
}


