import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Vounteer_Dashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField volunteerIdField;
    private JTextArea resultArea;

    // DB connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Akku703499@";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Vounteer_Dashboard frame = new Vounteer_Dashboard();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Vounteer_Dashboard() {
        setTitle("Volunteer Information Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 100, 600, 450);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(10, 10));

        // ---- Header ----
        JLabel headerLabel = new JLabel("Volunteer Search", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 102, 204));
        contentPane.add(headerLabel, BorderLayout.NORTH);

        // ---- Input Panel ----
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JLabel lblId = new JLabel("Enter Volunteer ID:");
        lblId.setFont(new Font("Arial", Font.PLAIN, 16));
        volunteerIdField = new JTextField(15);
        volunteerIdField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton btnSearch = new JButton("Search");
        btnSearch.setFont(new Font("Arial", Font.BOLD, 16));
        btnSearch.setBackground(new Color(0, 153, 76));
        btnSearch.setForeground(Color.WHITE);

        // ---- Home Button ----
        JButton btnHome = new JButton("Go to Home");
        btnHome.setFont(new Font("Arial", Font.BOLD, 14));
        btnHome.setForeground(Color.BLACK);   // black text
        btnHome.setBackground(new Color(220, 220, 220)); // light gray background

        inputPanel.add(lblId);
        inputPanel.add(volunteerIdField);
        inputPanel.add(btnSearch);
        inputPanel.add(btnHome);

        contentPane.add(inputPanel, BorderLayout.CENTER);

        // ---- Result Area ----
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        resultArea.setEditable(false);
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setPreferredSize(new Dimension(580, 200));
        contentPane.add(scrollPane, BorderLayout.SOUTH);

        // ---- Actions ----
        btnSearch.addActionListener(e -> fetchVolunteerDetails());

        btnHome.addActionListener(e -> {
            dispose(); // close current dashboard
            new Home_Page().setVisible(true); // open home (make sure Home_Page exists)
        });
    }

    private void fetchVolunteerDetails() {
        String volunteerId = volunteerIdField.getText().trim();

        if (volunteerId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Volunteer ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "SELECT * FROM volunteer WHERE Vid = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, volunteerId);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    StringBuilder details = new StringBuilder();
                    details.append("Volunteer Details\n");
                    details.append("=================\n");
                    details.append("ID: ").append(rs.getString("Vid")).append("\n");
                    details.append("Name: ").append(rs.getString("name")).append("\n");
                    details.append("Location: ").append(rs.getString("location")).append("\n");

                    resultArea.setText(details.toString());
                } else {
                    resultArea.setText("No volunteer found with ID: " + volunteerId);
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


