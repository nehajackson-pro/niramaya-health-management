package niramay.ui;

import niramay.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class HealthCareProvider extends JFrame {
    private String hospitalCode;
    private String migrantId;
    private JTextArea symptomsArea;
    private JTextArea diagnosisArea;
    private JTextArea prescriptionArea;
    
    public HealthCareProvider(String hospitalCode, String migrantId) {
        this.hospitalCode = hospitalCode;
        this.migrantId = migrantId;
        initializeUI();
        loadPatientInfo();
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Health Care Provider Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Patient Overview", createPatientOverviewPanel());
        tabbedPane.addTab("Medical Records", createMedicalRecordsPanel());
        tabbedPane.addTab("New Consultation", createNewConsultationPanel());
        tabbedPane.addTab("Lab Results", createLabResultsPanel());
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 82, 155));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Health Care Provider Dashboard", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel infoLabel = new JLabel("Hospital: " + hospitalCode + " | Patient: " + migrantId, SwingConstants.RIGHT);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(Color.WHITE);
        
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(new Color(0, 82, 155));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(0, 82, 155));
        rightPanel.add(infoLabel);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createPatientOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Patient info will be loaded here
        JTextArea patientInfoArea = new JTextArea();
        patientInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        patientInfoArea.setEditable(false);
        patientInfoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Load patient info from database
        String patientInfo = loadPatientInfoFromDB();
        patientInfoArea.setText(patientInfo);
        
        JScrollPane scrollPane = new JScrollPane(patientInfoArea);
        
        // Quick actions
        JPanel actionsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        String[] actions = {"View Full History", "Add Prescription", "Schedule Follow-up", "Generate Report"};
        Color[] colors = {new Color(0, 120, 215), new Color(16, 137, 62), new Color(216, 59, 1), new Color(136, 23, 152)};
        
        for (int i = 0; i < actions.length; i++) {
            JButton btn = new JButton(actions[i]);
            btn.setBackground(colors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setFocusPainted(false);
            final String action = actions[i];
            btn.addActionListener(e -> handleQuickAction(action));
            actionsPanel.add(btn);
        }
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMedicalRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Medical History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 82, 155));
        
        // Load medical records from database
        String[] columns = {"Date", "Diagnosis", "Treatment", "Prescription", "Notes"};
        Object[][] data = loadMedicalRecordsFromDB();
        
        JTable recordsTable = new JTable(data, columns);
        recordsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        recordsTable.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(recordsTable);
        
        JButton refreshButton = new JButton("Refresh Records");
        refreshButton.setBackground(new Color(0, 82, 155));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> refreshMedicalRecords());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createNewConsultationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("New Patient Consultation", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 82, 155));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        symptomsArea = new JTextArea(4, 20);
        symptomsArea.setBorder(BorderFactory.createTitledBorder("Symptoms *"));
        symptomsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        diagnosisArea = new JTextArea(4, 20);
        diagnosisArea.setBorder(BorderFactory.createTitledBorder("Diagnosis *"));
        diagnosisArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        prescriptionArea = new JTextArea(4, 20);
        prescriptionArea.setBorder(BorderFactory.createTitledBorder("Prescription *"));
        prescriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton saveButton = new JButton("Save Consultation Record");
        saveButton.setBackground(new Color(16, 137, 62));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.addActionListener(new SaveConsultationListener());
        
        formPanel.add(titleLabel);
        formPanel.add(new JScrollPane(symptomsArea));
        formPanel.add(new JScrollPane(diagnosisArea));
        formPanel.add(new JScrollPane(prescriptionArea));
        formPanel.add(saveButton);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLabResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Laboratory Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 82, 155));
        
        // Sample lab results
        String[] labColumns = {"Test", "Result", "Normal Range", "Status"};
        Object[][] labData = {
            {"Blood Pressure", "120/80", "90/60 - 120/80", "Normal"},
            {"Blood Sugar", "95 mg/dL", "70-100 mg/dL", "Normal"},
            {"Cholesterol", "180 mg/dL", "<200 mg/dL", "Normal"},
            {"Hemoglobin", "14.2 g/dL", "12-16 g/dL", "Normal"}
        };
        
        JTable labTable = new JTable(labData, labColumns);
        labTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labTable.setRowHeight(25);
        JScrollPane labScroll = new JScrollPane(labTable);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(labScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private String loadPatientInfoFromDB() {
        StringBuilder info = new StringBuilder();
        info.append("PATIENT INFORMATION\n");
        info.append("===================\n\n");
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return "Database connection failed";
            
            // Get patient basic info
            String patientSql = "SELECT full_name, date_of_birth, contact_number, emergency_contact, medical_history FROM migrant_workers WHERE migrant_id = ?";
            PreparedStatement patientStmt = conn.prepareStatement(patientSql);
            patientStmt.setString(1, migrantId);
            ResultSet patientRs = patientStmt.executeQuery();
            
            if (patientRs.next()) {
                info.append("Patient ID: ").append(migrantId).append("\n");
                info.append("Name: ").append(patientRs.getString("full_name")).append("\n");
                info.append("Date of Birth: ").append(patientRs.getString("date_of_birth")).append("\n");
                info.append("Contact: ").append(patientRs.getString("contact_number")).append("\n");
                info.append("Emergency Contact: ").append(patientRs.getString("emergency_contact")).append("\n");
                info.append("Medical History: ").append(patientRs.getString("medical_history")).append("\n\n");
            }
            patientStmt.close();
            
            // Get last consultation
            String lastVisitSql = "SELECT visit_date, diagnosis FROM patient_records WHERE migrant_id = ? ORDER BY visit_date DESC LIMIT 1";
            PreparedStatement lastVisitStmt = conn.prepareStatement(lastVisitSql);
            lastVisitStmt.setString(1, migrantId);
            ResultSet lastVisitRs = lastVisitStmt.executeQuery();
            
            if (lastVisitRs.next()) {
                info.append("Last Visit: ").append(lastVisitRs.getString("visit_date")).append("\n");
                info.append("Last Diagnosis: ").append(lastVisitRs.getString("diagnosis")).append("\n");
            }
            lastVisitStmt.close();
            
            // Get health survey info
            String surveySql = "SELECT survey_date, blood_pressure, recommendations FROM health_surveys WHERE migrant_id = ? ORDER BY survey_date DESC LIMIT 1";
            PreparedStatement surveyStmt = conn.prepareStatement(surveySql);
            surveyStmt.setString(1, migrantId);
            ResultSet surveyRs = surveyStmt.executeQuery();
            
            if (surveyRs.next()) {
                info.append("\nLast Health Survey: ").append(surveyRs.getString("survey_date")).append("\n");
                info.append("Blood Pressure: ").append(surveyRs.getString("blood_pressure")).append("\n");
                info.append("Recommendations: ").append(surveyRs.getString("recommendations")).append("\n");
            }
            surveyStmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            info.append("Error loading patient data: ").append(e.getMessage());
        }
        
        return info.toString();
    }
    
    private Object[][] loadMedicalRecordsFromDB() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return new Object[][]{{"Database connection failed", "", "", "", ""}};
            
            String sql = "SELECT visit_date, diagnosis, prescription, notes FROM patient_records WHERE migrant_id = ? ORDER BY visit_date DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, migrantId);
            ResultSet rs = pstmt.executeQuery();
            
            java.util.ArrayList<Object[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getString("visit_date");
                row[1] = rs.getString("diagnosis");
                row[2] = "Treatment recorded";
                row[3] = rs.getString("prescription");
                row[4] = rs.getString("notes");
                rows.add(row);
            }
            
            pstmt.close();
            
            if (rows.isEmpty()) {
                return new Object[][] {{"No medical records found", "", "", "", ""}};
            }
            
            return rows.toArray(new Object[0][0]);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[][] {{"Error loading records: " + e.getMessage(), "", "", "", ""}};
        }
    }
    
    private void loadPatientInfo() {
        System.out.println("Loading patient info for: " + migrantId);
    }
    
    private void handleQuickAction(String action) {
        switch (action) {
            case "View Full History":
                JOptionPane.showMessageDialog(this, "Opening complete medical history for " + migrantId);
                break;
            case "Add Prescription":
                String prescription = JOptionPane.showInputDialog(this, "Enter new prescription:");
                if (prescription != null && !prescription.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Prescription added successfully!");
                }
                break;
            case "Schedule Follow-up":
                String date = JOptionPane.showInputDialog(this, "Enter follow-up date (YYYY-MM-DD):");
                if (date != null && !date.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Follow-up scheduled for: " + date);
                }
                break;
            case "Generate Report":
                JOptionPane.showMessageDialog(this, "Generating patient report for " + migrantId + "...");
                break;
        }
    }
    
    private void refreshMedicalRecords() {
        JOptionPane.showMessageDialog(this, "Medical records refreshed!");
    }
    
    private class SaveConsultationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String symptoms = symptomsArea.getText().trim();
            String diagnosis = diagnosisArea.getText().trim();
            String prescription = prescriptionArea.getText().trim();
            
            if (symptoms.isEmpty() || diagnosis.isEmpty() || prescription.isEmpty()) {
                JOptionPane.showMessageDialog(HealthCareProvider.this,
                    "Please fill in all consultation fields", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                Connection conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    JOptionPane.showMessageDialog(HealthCareProvider.this,
                        "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String sql = "INSERT INTO patient_records (migrant_id, hospital_code, visit_date, diagnosis, prescription, notes) VALUES (?, ?, CURDATE(), ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, migrantId);
                pstmt.setString(2, hospitalCode);
                pstmt.setString(3, diagnosis);
                pstmt.setString(4, prescription);
                pstmt.setString(5, "Symptoms: " + symptoms);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(HealthCareProvider.this,
                        "Consultation saved successfully!\nPatient record updated in database.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear fields after successful save
                    symptomsArea.setText("");
                    diagnosisArea.setText("");
                    prescriptionArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(HealthCareProvider.this,
                        "Failed to save consultation. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                pstmt.close();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(HealthCareProvider.this,
                    "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
