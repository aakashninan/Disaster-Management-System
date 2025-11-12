import java.awt.*;
import javax.swing.*;

public class Home_Page extends JFrame {

    private static final long serialVersionUID = 1L;

    public Home_Page() {
        setTitle("Disaster Management System - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Buttons
        JButton btnDonor = new JButton("Donor Dashboard");
        JButton btnReliefCamp = new JButton("Relief Camp Page");
        JButton btnReports = new JButton("Reports & Updates");
        JButton btnResource = new JButton("Resource Management");
        JButton btnVictim = new JButton("Victim Assistance");
        JButton btnVolunteer = new JButton("Volunteer Dashboard");
        JButton btnLogout = new JButton("Logout");

        // Add buttons to panel
        panel.add(btnDonor);
        panel.add(btnReliefCamp);
        panel.add(btnReports);
        panel.add(btnResource);
        panel.add(btnVictim);
        panel.add(btnVolunteer);
        panel.add(btnLogout);

        add(panel);

        // Button actions
        btnDonor.addActionListener(e -> new Donor_Dashboard().setVisible(true));
        btnReliefCamp.addActionListener(e -> new Relief_Camp_Page().setVisible(true));
        btnReports.addActionListener(e -> new Reports_Updates_Page().setVisible(true));
        btnResource.addActionListener(e -> new Resource_Management_page().setVisible(true));
        btnVictim.addActionListener(e -> new Victim_Assistance_Page().setVisible(true));
        btnVolunteer.addActionListener(e -> new Vounteer_Dashboard().setVisible(true));

        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You have been logged out.");
            this.dispose();
            new User_Login().setVisible(true); // Go back to login
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Home_Page frame = new Home_Page();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
