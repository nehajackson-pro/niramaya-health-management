package niramay.ui;

import niramay.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MigrantWorkerLogin extends JFrame {
    private JTextField migrantIdField;
    private JPasswordField passwordField;
    
    public MigrantWorkerLogin() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Migrant Worker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Back button
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton);
        
        // Header
        JLabel headerLabel = new JLabel("Migrant Worker Portal", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(136, 23, 152));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        JLabel subHeaderLabel = new JLabel("Access your health records and services", SwingConstants.CENTER);
        subHeaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subHeaderLabel.setForeground(new Color(100, 100, 100));
        subHeaderLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        
        // Migrant ID
        JLabel migrantIdLabel = new JLabel("Migrant ID");
        migrantIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        migrantIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        migrantIdField = createTextField("MW001");
        
        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Login button
        JButton loginButton = new JButton("Login to Dashboard");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(136, 23, 152));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(new LoginButtonListener());
        
        // Sign up link
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setBackground(Color.WHITE);
        JLabel signupLabel = new JLabel("Don't have an account? ");
        JButton signupButton = new JButton("Sign up here");
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setForeground(new Color(136, 23, 152));
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        signupButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(e -> {
            new MigrantWorkerSignup().setVisible(true);
            dispose();
        });
        
        signupPanel.add(signupLabel);
        signupPanel.add(signupButton);
        signupPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add components to form panel with spacing
        formPanel.add(migrantIdLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(migrantIdField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(signupPanel);
        
        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(headerLabel, BorderLayout.NORTH);
        centerPanel.add(subHeaderLabel, BorderLayout.CENTER);
        centerPanel.add(formPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textField.setText(placeholder);
        return textField;
    }
    
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String migrantId = migrantIdField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (migrantId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(MigrantWorkerLogin.this,
                    "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(MigrantWorkerLogin.this,
                        "Database connection failed. Please check your database setup.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String sql = "SELECT * FROM migrant_workers WHERE migrant_id = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, migrantId);
                pstmt.setString(2, password);
                
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    new MigrantWorker(migrantId).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(MigrantWorkerLogin.this,
                        "Invalid migrant ID or password", "Authentication Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
                pstmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MigrantWorkerLogin.this,
                    "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
