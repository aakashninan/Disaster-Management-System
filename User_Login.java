import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class User_Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/disaster";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                User_Login frame = new User_Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public User_Login() {
        setTitle("User Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("User Login Page");
        lblTitle.setBounds(160, 20, 150, 16);
        contentPane.add(lblTitle);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(60, 80, 78, 16);
        contentPane.add(lblUsername);

        textField = new JTextField();
        textField.setBounds(160, 75, 150, 26);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(60, 125, 78, 16);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 120, 150, 26);
        contentPane.add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(160, 170, 117, 29);
        contentPane.add(btnLogin);

        JButton btnAdd = new JButton("Add User");
        btnAdd.setBounds(160, 210, 117, 29);
        contentPane.add(btnAdd);

        // Action listeners
        btnLogin.addActionListener(e -> verifyLogin());
        btnAdd.addActionListener(e -> addUser());
    }

    // Login Verification
    private void verifyLogin() {
        String username = textField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + username);

                    // ✅ Go to Home Page
                    Home_Page home = new Home_Page();
                    home.setVisible(true);
                    this.dispose();  // close login window
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password");
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

    // Add New User
    private void addUser() {
        String username = textField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, password);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "User Registered Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to register user.");
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

