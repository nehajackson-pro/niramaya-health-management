package niramay.ui;

import niramay.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CommunityHealthWorker extends JFrame {
    private String workerId;
    private String migrantId;
    private String workerRegion;
    private JCheckBox feverCheck, coughCheck, breathingCheck, fatigueCheck;
    private JTextField temperatureField, bloodPressureField;
    private JTextArea symptomsArea, recommendationsArea;
    private JTextField[] vitalFields;
    
    public CommunityHealthWorker(String workerId, String migrantId) {
        this.workerId = workerId;
        this.migrantId = migrantId;
        loadWorkerRegion();
        initializeUI();
    }
    
    private void loadWorkerRegion() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return;
            
            String sql = "SELECT region, full_name FROM community_health_workers WHERE worker_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, workerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                workerRegion = rs.getString("region");
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            workerRegion = "Unknown Region";
        }
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Community Health Worker Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Health Survey", createHealthSurveyPanel());
        tabbedPane.addTab("Vital Signs", createVitalSignsPanel());
        tabbedPane.addTab("Health Education", createHealthEducationPanel());
        tabbedPane.addTab("Field Reports", createFieldReportsPanel());
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(16, 137, 62));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Community Health Worker Dashboard", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        // Enhanced info label with region
        JLabel infoLabel = new JLabel("<html>Worker: " + workerId + 
                                    " | Region: " + workerRegion + 
                                    " | Current Migrant: " + migrantId + "</html>", 
                                    SwingConstants.RIGHT);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(Color.WHITE);
        
        // Add migrant switcher button
        JButton switchMigrantButton = new JButton("Switch Migrant");
        switchMigrantButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        switchMigrantButton.setBackground(Color.WHITE);
        switchMigrantButton.setForeground(new Color(16, 137, 62));
        switchMigrantButton.setFocusPainted(false);
        switchMigrantButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        switchMigrantButton.addActionListener(e -> switchMigrantWorker());
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(new Color(16, 137, 62));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(16, 137, 62));
        rightPanel.add(infoLabel);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(switchMigrantButton);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createHealthSurveyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Health Assessment Survey", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(16, 137, 62));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        // Survey questions
        feverCheck = new JCheckBox("Fever");
        coughCheck = new JCheckBox("Cough");
        breathingCheck = new JCheckBox("Breathing Difficulty");
        fatigueCheck = new JCheckBox("Fatigue");
        
        temperatureField = new JTextField();
        temperatureField.setBorder(BorderFactory.createTitledBorder("Temperature (°C)"));
        
        bloodPressureField = new JTextField();
        bloodPressureField.setBorder(BorderFactory.createTitledBorder("Blood Pressure"));
        
        symptomsArea = new JTextArea(3, 20);
        symptomsArea.setBorder(BorderFactory.createTitledBorder("Other Symptoms"));
        symptomsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        recommendationsArea = new JTextArea(3, 20);
        recommendationsArea.setBorder(BorderFactory.createTitledBorder("Recommendations"));
        recommendationsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton submitButton = new JButton("Submit Health Survey");
        submitButton.setBackground(new Color(16, 137, 62));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.addActionListener(new SubmitSurveyListener());
        
        formPanel.add(titleLabel);
        formPanel.add(feverCheck);
        formPanel.add(coughCheck);
        formPanel.add(breathingCheck);
        formPanel.add(fatigueCheck);
        formPanel.add(temperatureField);
        formPanel.add(bloodPressureField);
        formPanel.add(new JScrollPane(symptomsArea));
        formPanel.add(new JScrollPane(recommendationsArea));
        formPanel.add(submitButton);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createVitalSignsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Vital Signs Monitoring", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(16, 137, 62));
        
        // Vital signs input form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        String[] vitalLabels = {"Blood Pressure:", "Heart Rate (bpm):", "Oxygen Saturation (%):", "Respiratory Rate:"};
        vitalFields = new JTextField[4];
        
        for (int i = 0; i < vitalLabels.length; i++) {
            JLabel label = new JLabel(vitalLabels[i]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            formPanel.add(label);
            vitalFields[i] = new JTextField();
            vitalFields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            formPanel.add(vitalFields[i]);
        }
        
        JButton recordButton = new JButton("Record Vital Signs");
        recordButton.setBackground(new Color(16, 137, 62));
        recordButton.setForeground(Color.WHITE);
        recordButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        recordButton.addActionListener(new RecordVitalsListener());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(recordButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHealthEducationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Health Education Materials", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(16, 137, 62));
        
        String[] topics = {
            "✓ Hygiene and Sanitation Practices",
            "✓ Nutrition and Balanced Diet", 
            "✓ Preventive Healthcare Measures",
            "✓ Mental Health Awareness",
            "✓ First Aid Basics",
            "✓ Chronic Disease Management",
            "✓ Maternal and Child Health",
            "✓ Infectious Disease Prevention"
        };
        
        JTextArea educationArea = new JTextArea();
        educationArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        educationArea.setEditable(false);
        for (String topic : topics) {
            educationArea.append(topic + "\n\n");
        }
        
        JScrollPane scrollPane = new JScrollPane(educationArea);
        
        JButton printButton = new JButton("Print Education Materials");
        printButton.setBackground(new Color(16, 137, 62));
        printButton.setForeground(Color.WHITE);
        printButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(CommunityHealthWorker.this, "Printing education materials..."));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(printButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFieldReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Field Visit Reports", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(16, 137, 62));
        
        // Load reports from database
        String[] columns = {"Date", "Migrant ID", "Health Status", "Follow-up Needed"};
        Object[][] data = loadFieldReportsFromDB();
        
        JTable reportsTable = new JTable(data, columns);
        reportsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportsTable.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(reportsTable);
        
        JButton newReportButton = new JButton("Create New Field Report");
        newReportButton.setBackground(new Color(16, 137, 62));
        newReportButton.setForeground(Color.WHITE);
        newReportButton.addActionListener(e -> createNewFieldReport());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(newReportButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Object[][] loadFieldReportsFromDB() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return new Object[][]{{"Database connection failed", "", "", ""}};
            
            String sql = "SELECT survey_date, migrant_id, recommendations, symptoms FROM health_surveys WHERE worker_id = ? ORDER BY survey_date DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, workerId);
            ResultSet rs = pstmt.executeQuery();
            
            java.util.ArrayList<Object[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getString("survey_date");
                row[1] = rs.getString("migrant_id");
                row[2] = rs.getString("recommendations").isEmpty() ? "Good" : "Needs Attention";
                row[3] = rs.getString("symptoms").contains("Fever") ? "Yes" : "No";
                rows.add(row);
            }
            
            pstmt.close();
            
            if (rows.isEmpty()) {
                return new Object[][] {{"No field reports found", "", "", ""}};
            }
            
            return rows.toArray(new Object[0][0]);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[][] {{"Error loading reports: " + e.getMessage(), "", "", ""}};
        }
    }
    
    private void createNewFieldReport() {
        JOptionPane.showMessageDialog(this, "Creating new field report form...");
    }
    
    private void switchMigrantWorker() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return;
            
            // Get migrant workers from the same region
            String migrantSql = "SELECT migrant_id, full_name FROM migrant_workers WHERE region = ? ORDER BY full_name";
            PreparedStatement migrantStmt = conn.prepareStatement(migrantSql);
            migrantStmt.setString(1, workerRegion);
            ResultSet migrantRs = migrantStmt.executeQuery();
            
            // Create migrant selection dialog
            JDialog selectionDialog = new JDialog(this, "Switch Migrant Worker", true);
            selectionDialog.setSize(500, 400);
            selectionDialog.setLocationRelativeTo(this);
            selectionDialog.setLayout(new BorderLayout());
            selectionDialog.setResizable(false);
            
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(Color.WHITE);
            
            JLabel titleLabel = new JLabel("Select Different Migrant Worker", JLabel.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(new Color(16, 137, 62));
            
            JLabel infoLabel = new JLabel("<html><b>Your Region:</b> " + workerRegion + "<br>" +
                                        "<b>Current Migrant:</b> " + migrantId + "</html>", JLabel.CENTER);
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
                migrantListModel.addElement("No other migrant workers found in your region");
            }
            
            JList<String> migrantList = new JList<>(migrantListModel);
            migrantList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            migrantList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            migrantList.setBackground(Color.WHITE);
            JScrollPane listScroll = new JScrollPane(migrantList);
            listScroll.setBorder(BorderFactory.createTitledBorder("Available Migrant Workers in Your Region"));
            
            JButton selectButton = new JButton("Switch to Selected Migrant");
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
                
                // Close current dashboard and open new one with selected migrant
                dispose();
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
            JOptionPane.showMessageDialog(this,
                "Error loading migrant workers: " + ex.getMessage(), "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private class SubmitSurveyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Collect survey data
            StringBuilder symptomsBuilder = new StringBuilder();
            if (feverCheck.isSelected()) symptomsBuilder.append("Fever, ");
            if (coughCheck.isSelected()) symptomsBuilder.append("Cough, ");
            if (breathingCheck.isSelected()) symptomsBuilder.append("Breathing Difficulty, ");
            if (fatigueCheck.isSelected()) symptomsBuilder.append("Fatigue, ");
            
            String checkboxSymptoms = symptomsBuilder.toString();
            String otherSymptoms = symptomsArea.getText().trim();
            String allSymptoms = checkboxSymptoms + otherSymptoms;
            if (allSymptoms.endsWith(", ")) {
                allSymptoms = allSymptoms.substring(0, allSymptoms.length() - 2);
            }
            
            String temperature = temperatureField.getText().trim();
            String bloodPressure = bloodPressureField.getText().trim();
            String recommendations = recommendationsArea.getText().trim();
            
            if (allSymptoms.trim().isEmpty() && recommendations.isEmpty()) {
                JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                    "Please fill in at least symptoms or recommendations", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                        "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String sql = "INSERT INTO health_surveys (migrant_id, worker_id, survey_date, blood_pressure, temperature, symptoms, recommendations) VALUES (?, ?, CURDATE(), ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, migrantId);
                pstmt.setString(2, workerId);
                pstmt.setString(3, bloodPressure);
                pstmt.setString(4, temperature.isEmpty() ? null : temperature);
                pstmt.setString(5, allSymptoms);
                pstmt.setString(6, recommendations);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                        "Health survey submitted successfully!\nData saved to database.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear form after successful submission
                    feverCheck.setSelected(false);
                    coughCheck.setSelected(false);
                    breathingCheck.setSelected(false);
                    fatigueCheck.setSelected(false);
                    temperatureField.setText("");
                    bloodPressureField.setText("");
                    symptomsArea.setText("");
                    recommendationsArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                        "Failed to submit survey. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                pstmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                    "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private class RecordVitalsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String bloodPressure = vitalFields[0].getText().trim();
            String heartRate = vitalFields[1].getText().trim();
            String oxygenSaturation = vitalFields[2].getText().trim();
            String respiratoryRate = vitalFields[3].getText().trim();
            
            if (bloodPressure.isEmpty() && heartRate.isEmpty() && oxygenSaturation.isEmpty() && respiratoryRate.isEmpty()) {
                JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                    "Please enter at least one vital sign", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                        "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String sql = "INSERT INTO health_surveys (migrant_id, worker_id, survey_date, blood_pressure, symptoms) VALUES (?, ?, CURDATE(), ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, migrantId);
                pstmt.setString(2, workerId);
                pstmt.setString(3, bloodPressure);
                
                StringBuilder vitalsSymptoms = new StringBuilder("Vitals: ");
                if (!heartRate.isEmpty()) vitalsSymptoms.append("HR:").append(heartRate).append(", ");
                if (!oxygenSaturation.isEmpty()) vitalsSymptoms.append("O2:").append(oxygenSaturation).append("%, ");
                if (!respiratoryRate.isEmpty()) vitalsSymptoms.append("RR:").append(respiratoryRate);
                
                pstmt.setString(4, vitalsSymptoms.toString());
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                        "Vital signs recorded successfully!\nData saved to database.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear fields after successful recording
                    for (JTextField field : vitalFields) {
                        field.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                        "Failed to record vital signs. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                pstmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(CommunityHealthWorker.this,
                    "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
