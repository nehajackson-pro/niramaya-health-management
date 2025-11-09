package niramay.ui;

import niramay.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CommunityHealthWorkerLogin extends JFrame {
    private JTextField workerIdField;
    private JPasswordField passwordField;
    
    public CommunityHealthWorkerLogin() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Community Health Worker");
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
        JLabel headerLabel = new JLabel("Community Health Worker Portal", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(16, 137, 62));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        JLabel subHeaderLabel = new JLabel("Conduct health surveys and monitor community health", SwingConstants.CENTER);
        subHeaderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subHeaderLabel.setForeground(new Color(100, 100, 100));
        subHeaderLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        
        // Worker ID
        JLabel workerIdLabel = new JLabel("Worker ID");
        workerIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        workerIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        workerIdField = createTextField("CHW001");
        
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
        loginButton.setBackground(new Color(16, 137, 62));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(new LoginButtonListener());
        
        // Add components to form panel with spacing
        formPanel.add(workerIdLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(workerIdField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginButton);
        
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
            String workerId = workerIdField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (workerId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(CommunityHealthWorkerLogin.this,
                    "Please fill in all fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(CommunityHealthWorkerLogin.this,
                        "Database connection failed. Please check your database setup.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Verify worker credentials
                String sql = "SELECT * FROM community_health_workers WHERE worker_id = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, workerId);
                pstmt.setString(2, password);
                
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next()) {
                    // Get worker's region
                    String workerRegion = rs.getString("region");
                    String workerName = rs.getString("full_name");
                    
                    // Show migrant selection dialog
                    showMigrantSelectionDialog(workerId, workerName, workerRegion);
                } else {
                    JOptionPane.showMessageDialog(CommunityHealthWorkerLogin.this,
                        "Invalid worker ID or password", "Authentication Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
                pstmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(CommunityHealthWorkerLogin.this,
                    "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        
        private void showMigrantSelectionDialog(String workerId, String workerName, String workerRegion) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) return;
                
                // Get migrant workers from the same region
                String migrantSql = "SELECT migrant_id, full_name FROM migrant_workers WHERE region = ? ORDER BY full_name";
                PreparedStatement migrantStmt = conn.prepareStatement(migrantSql);
                migrantStmt.setString(1, workerRegion);
                ResultSet migrantRs = migrantStmt.executeQuery();
                
                // Create migrant selection dialog
                JDialog selectionDialog = new JDialog(CommunityHealthWorkerLogin.this, "Select Migrant Worker", true);
                selectionDialog.setSize(500, 400);
                selectionDialog.setLocationRelativeTo(CommunityHealthWorkerLogin.this);
                selectionDialog.setLayout(new BorderLayout());
                selectionDialog.setResizable(false);
                
                JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
                contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                contentPanel.setBackground(Color.WHITE);
                
                JLabel titleLabel = new JLabel("Select Migrant Worker for Health Survey", JLabel.CENTER);
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                titleLabel.setForeground(new Color(16, 137, 62));
                
                JLabel infoLabel = new JLabel("<html><b>Worker:</b> " + workerName + " (" + workerId + ")<br>" +
                                            "<b>Region:</b> " + workerRegion + "</html>", JLabel.CENTER);
                infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                infoLabel.setForeground(Color.GRAY);
                infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                
                // Create migrant list
                DefaultListModel<String> migrantListModel = new DefaultListModel<>();
                java.util.ArrayList<String> migrantIds = new java.util.ArrayList<>();
                
                boolean hasMigrants = false;
                while (migrantRs.next()) {
                    String migrantId = migrantRs.getString("migrant_id");
                    String fullName = migrantRs.getString("full_name");
                    migrantListModel.addElement(migrantId + " - " + fullName);
                    migrantIds.add(migrantId);
                    hasMigrants = true;
                }
                
                if (!hasMigrants) {
                    migrantListModel.addElement("No migrant workers found in your region (" + workerRegion + ")");
                }
                
                JList<String> migrantList = new JList<>(migrantListModel);
                migrantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                migrantList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                migrantList.setBackground(Color.WHITE);
                JScrollPane listScroll = new JScrollPane(migrantList);
                listScroll.setBorder(BorderFactory.createTitledBorder("Available Migrant Workers"));
                
                JButton selectButton = new JButton("Select Migrant Worker");
                selectButton.setBackground(new Color(16, 137, 62));
                selectButton.setForeground(Color.WHITE);
                selectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
                selectButton.setEnabled(hasMigrants);
                selectButton.addActionListener(evt -> {
                    int selectedIndex = migrantList.getSelectedIndex();
                    if (selectedIndex == -1) {
                        JOptionPane.showMessageDialog(selectionDialog,
                            "Please select a migrant worker", "Selection Required", 
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String selectedMigrantId = migrantIds.get(selectedIndex);
                    selectionDialog.dispose();
                    dispose(); // Close login window
                    
                    // Open community health worker dashboard
                    new CommunityHealthWorker(workerId, selectedMigrantId).setVisible(true);
                });
                
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setBackground(Color.GRAY);
                cancelButton.setForeground(Color.WHITE);
                cancelButton.addActionListener(evt -> selectionDialog.dispose());
                
                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.setBackground(Color.WHITE);
                buttonPanel.add(selectButton);
                buttonPanel.add(cancelButton);
                
                contentPanel.add(titleLabel, BorderLayout.NORTH);
                contentPanel.add(infoLabel, BorderLayout.CENTER);
                contentPanel.add(listScroll, BorderLayout.CENTER);
                contentPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                selectionDialog.add(contentPanel);
                selectionDialog.setVisible(true);
                
                migrantStmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(CommunityHealthWorkerLogin.this,
                    "Error loading migrant workers: " + ex.getMessage(), "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
