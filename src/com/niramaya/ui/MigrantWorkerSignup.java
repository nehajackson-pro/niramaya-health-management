package niramay.ui;

import niramay.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MigrantWorkerSignup extends JFrame {
    private JTextField fullNameField;
    private JTextField migrantIdField;
    private JPasswordField passwordField;
    private JTextField dobField;
    private JTextField contactField;
    private JTextField emergencyContactField;
    private JTextArea medicalHistoryArea;
    private JComboBox<String> regionCombo;
    
    public MigrantWorkerSignup() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Migrant Worker Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Back button
        JButton backButton = new JButton("← Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.addActionListener(e -> {
            new MigrantWorkerLogin().setVisible(true);
            dispose();
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton);
        
        // Header
        JLabel headerLabel = new JLabel("Create Migrant Worker Account", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(136, 23, 152));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        // Form panel with two columns
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 30, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        
        // Left column
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        // Right column
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        // Full Name
        addFormField(leftPanel, "Full Name *", fullNameField = createTextField("Amit Singh"));
        
        // Migrant ID
        addFormField(leftPanel, "Migrant ID *", migrantIdField = createTextField("MW005"));
        
        // Password
        JLabel passwordLabel = new JLabel("Password *");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        leftPanel.add(passwordLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(passwordField);
        leftPanel.add(Box.createVerticalStrut(20));
        
        // Date of Birth
        addFormField(leftPanel, "Date of Birth * (YYYY-MM-DD)", dobField = createTextField("1990-05-15"));
        
        // Contact Number
        addFormField(rightPanel, "Contact Number *", contactField = createTextField("9876543210"));
        
        // Emergency Contact
        addFormField(rightPanel, "Emergency Contact *", emergencyContactField = createTextField("9876543211"));
        
        // Region
        JLabel regionLabel = new JLabel("Region *");
        regionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        regionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        regionCombo = new JComboBox<>(new String[]{
            "North Region", "South Region", "East Region", "West Region", "Central Region"
        });
        regionCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        regionCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rightPanel.add(regionLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(regionCombo);
        rightPanel.add(Box.createVerticalStrut(20));
        
        // Medical History
        JLabel medicalHistoryLabel = new JLabel("Medical History");
        medicalHistoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        medicalHistoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        medicalHistoryArea = new JTextArea(4, 20);
        medicalHistoryArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        medicalHistoryArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        medicalHistoryArea.setLineWrap(true);
        medicalHistoryArea.setWrapStyleWord(true);
        medicalHistoryArea.setText("No significant medical history");
        JScrollPane scrollPane = new JScrollPane(medicalHistoryArea);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        rightPanel.add(medicalHistoryLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(scrollPane);
        
        // Add columns to form panel
        formPanel.add(leftPanel);
        formPanel.add(rightPanel);
        
        // Sign up button
        JButton signupButton = new JButton("Create Account");
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        signupButton.setBackground(new Color(136, 23, 152));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        signupButton.addActionListener(new SignupButtonListener());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(signupButton);
        
        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(headerLabel, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void addFormField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(textField);
        panel.add(Box.createVerticalStrut(20));
    }
    
    private void addFormField(JPanel panel, String labelText, JComboBox<String> comboBox) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(comboBox);
        panel.add(Box.createVerticalStrut(20));
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
    
    private class SignupButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String fullName = fullNameField.getText().trim();
            String migrantId = migrantIdField.getText().trim();
            String password = new String(passwordField.getPassword());
            String dob = dobField.getText().trim();
            String contact = contactField.getText().trim();
            String emergencyContact = emergencyContactField.getText().trim();
            String region = regionCombo.getSelectedItem().toString();
            String medicalHistory = medicalHistoryArea.getText().trim();
            
            // Validation
            if (fullName.isEmpty() || migrantId.isEmpty() || password.isEmpty() || 
                dob.isEmpty() || contact.isEmpty() || emergencyContact.isEmpty()) {
                JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                    "Please fill in all required fields (*)", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                    "Password must be at least 6 characters long", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validate date format
            if (!isValidDate(dob)) {
                JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                    "Invalid date format. Please use YYYY-MM-DD format (e.g., 1990-05-15)",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                        "Database connection failed. Please check your database setup.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if migrant ID already exists
                String checkSql = "SELECT * FROM migrant_workers WHERE migrant_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, migrantId);
                ResultSet checkRs = checkStmt.executeQuery();
                
                if (checkRs.next()) {
                    JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                        "Migrant ID already exists. Please choose a different ID.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    checkStmt.close();
                    return;
                }
                checkStmt.close();
                
                // Insert new migrant worker
                String insertSql = "INSERT INTO migrant_workers (migrant_id, full_name, password, date_of_birth, " +
                                 "contact_number, emergency_contact, medical_history, region) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, migrantId);
                insertStmt.setString(2, fullName);
                insertStmt.setString(3, password);
                insertStmt.setString(4, dob);
                insertStmt.setString(5, contact);
                insertStmt.setString(6, emergencyContact);
                insertStmt.setString(7, medicalHistory);
                insertStmt.setString(8, region);
                
                int rowsAffected = insertStmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                        "Account created successfully!\n\nYour Migrant ID: " + migrantId +
                        "\nRegion: " + region +
                        "\nYou can now login with your credentials.",
                        "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                    new MigrantWorkerLogin().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                        "Failed to create account. Please try again.",
                        "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
                
                insertStmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(MigrantWorkerSignup.this,
                    "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        
        private boolean isValidDate(String date) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                sdf.parse(date);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
