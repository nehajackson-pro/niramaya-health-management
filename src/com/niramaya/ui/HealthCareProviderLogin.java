package niramay.ui;

import niramay.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HealthCareProviderLogin extends JFrame {
    private JTextField hospitalCodeField;
    private JPasswordField passwordField;
    private JTextField migrantIdField;
    
    public HealthCareProviderLogin() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Health Care Provider");
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
        JLabel headerLabel = new JLabel("Health Care Provider Portal", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 82, 155));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        JLabel subHeaderLabel = new JLabel("Access patient medical records and provide care", SwingConstants.CENTER);
        subHeaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subHeaderLabel.setForeground(new Color(100, 100, 100));
        subHeaderLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        
        // Hospital Code
        JLabel hospitalCodeLabel = new JLabel("Hospital Code");
        hospitalCodeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        hospitalCodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        hospitalCodeField = createTextField("HOSP001");
        
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
        
        // Migrant ID
        JLabel migrantIdLabel = new JLabel("Patient Migrant ID");
        migrantIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        migrantIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        migrantIdField = createTextField("MW001");
        
        // Access button
        JButton accessButton = new JButton("Access Patient Dashboard");
        accessButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        accessButton.setBackground(new Color(0, 120, 215));
        accessButton.setForeground(Color.WHITE);
        accessButton.setFocusPainted(false);
        accessButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        accessButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        accessButton.addActionListener(new AccessButtonListener());
        
        // Add components to form panel with spacing
        formPanel.add(hospitalCodeLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(hospitalCodeField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(migrantIdLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(migrantIdField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(accessButton);
        
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
    
    private class AccessButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String hospitalCode = hospitalCodeField.getText().trim();
            String password = new String(passwordField.getPassword());
            String migrantId = migrantIdField.getText().trim();
            
            if (hospitalCode.isEmpty() || password.isEmpty() || migrantId.isEmpty()) {
                JOptionPane.showMessageDialog(HealthCareProviderLogin.this,
                    "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(HealthCareProviderLogin.this,
                        "Database connection failed. Please check your database setup.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String sql = "SELECT * FROM health_care_providers WHERE hospital_code = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, hospitalCode);
                pstmt.setString(2, password);
                
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // Verify migrant exists
                    String migrantSql = "SELECT * FROM migrant_workers WHERE migrant_id = ?";
                    PreparedStatement migrantStmt = conn.prepareStatement(migrantSql);
                    migrantStmt.setString(1, migrantId);
                    ResultSet migrantRs = migrantStmt.executeQuery();
                    
                    if (migrantRs.next()) {
                        new HealthCareProvider(hospitalCode, migrantId).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(HealthCareProviderLogin.this,
                            "Migrant ID not found in system", "Patient Not Found", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    migrantStmt.close();
                } else {
                    JOptionPane.showMessageDialog(HealthCareProviderLogin.this,
                        "Invalid hospital code or password", "Authentication Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
                pstmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(HealthCareProviderLogin.this,
                    "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
