package niramay.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomeFrame extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(0, 82, 155);
    private static final Color SECONDARY_COLOR = new Color(16, 137, 62);
    private static final Color ACCENT_COLOR = new Color(136, 23, 152);
    
    public WelcomeFrame() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Healthcare Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); // Reduced height from 700 to 600
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header section
        JPanel headerPanel = createHeaderPanel();
        
        // Role selection section
        JPanel rolesPanel = createRolesPanel();
        
        // Footer section
        JPanel footerPanel = createFooterPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(rolesPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 15, 40)); // Reduced padding
        
        JLabel titleLabel = new JLabel("NIRAMAYA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel subtitleLabel = new JLabel("Comprehensive Healthcare Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        JLabel roleTitleLabel = new JLabel("Choose your Role to Continue", SwingConstants.CENTER);
        roleTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Reduced font size
        roleTitleLabel.setForeground(new Color(60, 60, 60));
        roleTitleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0)); // Reduced padding
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        headerPanel.add(roleTitleLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createRolesPanel() {
        JPanel rolesPanel = new JPanel();
        rolesPanel.setLayout(new BoxLayout(rolesPanel, BoxLayout.Y_AXIS));
        rolesPanel.setBackground(Color.WHITE);
        rolesPanel.setBorder(BorderFactory.createEmptyBorder(5, 100, 15, 100)); // Reduced bottom padding
        
        String[] roles = {"Health Care Provider", "Community Health Worker", "Migrant Worker"};
        String[] descriptions = {
            "Access patient records, manage treatments, and provide healthcare services",
            "Conduct health surveys, monitor vital signs, and provide community health services", 
            "View personal health records, book appointments, and access health information"
        };
        Color[] colors = {PRIMARY_COLOR, SECONDARY_COLOR, ACCENT_COLOR};
        String[] icons = {"🏥", "👨‍⚕️", "👨‍💼"};
        
        for (int i = 0; i < roles.length; i++) {
            JPanel roleCard = createRoleCard(roles[i], descriptions[i], colors[i], icons[i]);
            rolesPanel.add(roleCard);
            
            if (i < roles.length - 1) {
                rolesPanel.add(Box.createVerticalStrut(10)); // Reduced gap from 15 to 10
            }
        }
        
        return rolesPanel;
    }
    
    private JPanel createRoleCard(String role, String description, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20) // Reduced padding from 25 to 15,20,15,20
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setMaximumSize(new Dimension(600, 80)); // Reduced height from 120 to 80
        
        // Header with icon and role
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24)); // Reduced icon size from 32 to 24
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10)); // Reduced padding
        
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Reduced font size from 18 to 16
        roleLabel.setForeground(color);
        
        JPanel iconRolePanel = new JPanel(new BorderLayout());
        iconRolePanel.setBackground(Color.WHITE);
        iconRolePanel.add(iconLabel, BorderLayout.WEST);
        iconRolePanel.add(roleLabel, BorderLayout.CENTER);
        
        // Description with smaller font
        JTextArea descArea = new JTextArea(description);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Reduced font size from 14 to 12
        descArea.setForeground(new Color(100, 100, 100));
        descArea.setBackground(Color.WHITE);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0)); // Reduced padding from 15 to 8
        
        card.add(iconRolePanel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);
        
        // Add click listener with updated border
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                handleRoleSelection(role);
            }
            public void mouseEntered(MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20) // Consistent with reduced padding
                ));
            }
            public void mouseExited(MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230)),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20) // Consistent with reduced padding
                ));
            }
        });
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Reduced padding
        
        JLabel footerLabel = new JLabel("Secure • Reliable • Accessible • Comprehensive", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        footerLabel.setForeground(new Color(150, 150, 150));
        
        footerPanel.add(footerLabel, BorderLayout.CENTER);
        
        return footerPanel;
    }
    
    private void handleRoleSelection(String role) {
        setVisible(false);
        
        switch (role) {
            case "Health Care Provider":
                SwingUtilities.invokeLater(() -> {
                    new HealthCareProviderLogin().setVisible(true);
                });
                break;
            case "Community Health Worker":
                SwingUtilities.invokeLater(() -> {
                    new CommunityHealthWorkerLogin().setVisible(true);
                });
                break;
            case "Migrant Worker":
                SwingUtilities.invokeLater(() -> {
                    new MigrantWorkerLogin().setVisible(true);
                });
                break;
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomeFrame().setVisible(true);
        });
    }
}
