import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Victim_Assistance_Page extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField nameField;
    private JTextField locationField;
    private JTextArea helpField;
    private JLabel messageLabel;

    // DB credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Akku703499@";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Victim_Assistance_Page frame = new Victim_Assistance_Page();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Victim_Assistance_Page() {
        setTitle("Victim Assistance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 450);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new GridBagLayout());
        contentPane.setBackground(new Color(240, 248, 255));
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Victim Assistance Form");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPane.add(titleLabel, gbc);

        // Name Label
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(new JLabel("Name:"), gbc);

        // Name Field
        gbc.gridx = 1;
        nameField = new JTextField();
        contentPane.add(nameField, gbc);

        // Location Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(new JLabel("Location:"), gbc);

        // Location Field
        gbc.gridx = 1;
        locationField = new JTextField();
        contentPane.add(locationField, gbc);

        // Help Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(new JLabel("Help Required:"), gbc);

        // Help Field
        gbc.gridx = 1;
        helpField = new JTextArea(4, 20);
        helpField.setLineWrap(true);
        helpField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(helpField);
        contentPane.add(scrollPane, gbc);

        // Submit Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setBackground(new Color(100, 149, 237));
        submitBtn.setForeground(Color.BLACK);  // text color black
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPane.add(submitBtn, gbc);

        // Back Button
        gbc.gridx = 1;
        JButton backBtn = new JButton("Back to Home");
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(Color.BLACK);  // text color black
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        contentPane.add(backBtn, gbc);

        // Message Label
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messageLabel.setForeground(new Color(34, 139, 34));
        contentPane.add(messageLabel, gbc);

        // Button action
        submitBtn.addActionListener(e -> saveVictimDetails());

        // Back Button Action
        backBtn.addActionListener(e -> {
            dispose();  // Close this page
            new Home_Page().setVisible(true); // Open Home Page (make sure Home_Page.java exists)
        });
    }

    private void saveVictimDetails() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String help = helpField.getText().trim();

        if (name.isEmpty() || location.isEmpty() || help.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "INSERT INTO victim (name, location, help) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, location);
                pst.setString(3, help);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    messageLabel.setText("✅ Help is on the way!");
                    nameField.setText("");
                    locationField.setText("");
                    helpField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to submit request.");
                }
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "MySQL JDBC Driver not found!");
            ex.printStackTrace();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}


