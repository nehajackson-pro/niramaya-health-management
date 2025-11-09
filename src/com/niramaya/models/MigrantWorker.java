package niramay.ui;

import niramay.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MigrantWorker extends JFrame {
    private String migrantId;
    private JTextArea profileArea;
    
    public MigrantWorker(String migrantId) {
        this.migrantId = migrantId;
        initializeUI();
        loadPersonalInfo();
    }
    
    private void initializeUI() {
        setTitle("NIRAMAYA - Migrant Worker Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("My Health Profile", createHealthProfilePanel());
        tabbedPane.addTab("Medical History", createMedicalHistoryPanel());
        tabbedPane.addTab("Appointments", createAppointmentsPanel());
        tabbedPane.addTab("Health Tips", createHealthTipsPanel());
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(136, 23, 152));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Migrant Worker Dashboard", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome! Migrant ID: " + migrantId, SwingConstants.RIGHT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcomeLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(new Color(136, 23, 152));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(136, 23, 152));
        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createHealthProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Health Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(136, 23, 152));
        
        // Personal information
        profileArea = new JTextArea();
        profileArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        profileArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(profileArea);
        
        // Quick actions
        JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        String[] actions = {"Update Information", "Request Appointment", "Download Health Card"};
        for (String action : actions) {
            JButton btn = new JButton(action);
            btn.setBackground(new Color(136, 23, 152));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.addActionListener(e -> handleProfileAction(action));
            actionsPanel.add(btn);
        }
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMedicalHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Medical History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(136, 23, 152));
        
        // Load medical history from database
        String[] history = loadMedicalHistoryFromDB();
        
        JList<String> historyList = new JList<>(history);
        historyList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyList.setBackground(Color.WHITE);
        JScrollPane listScroll = new JScrollPane(historyList);
        
        JButton refreshButton = new JButton("Refresh History");
        refreshButton.setBackground(new Color(136, 23, 152));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> refreshMedicalHistory());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(listScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("My Appointments", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(136, 23, 152));
        
        // Load appointments from database
        String[] columns = {"Date", "Time", "Doctor", "Purpose", "Status"};
        Object[][] data = loadAppointmentsFromDB();
        
        JTable appointmentsTable = new JTable(data, columns);
        appointmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appointmentsTable.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(appointmentsTable);
        
        JButton newAppointmentButton = new JButton("Book New Appointment");
        newAppointmentButton.setBackground(new Color(136, 23, 152));
        newAppointmentButton.setForeground(Color.WHITE);
        newAppointmentButton.addActionListener(new BookAppointmentListener());
        
        JButton refreshButton = new JButton("Refresh Appointments");
        refreshButton.setBackground(new Color(100, 100, 100));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> refreshAppointments());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(newAppointmentButton);
        buttonPanel.add(refreshButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHealthTipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Health Tips & Education", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(136, 23, 152));
        
        JTextArea tipsArea = new JTextArea();
        tipsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tipsArea.setEditable(false);
        tipsArea.setText("HEALTH TIPS FOR MIGRANT WORKERS\n\n");
        tipsArea.append("1. Stay Hydrated: Drink at least 8 glasses of water daily\n\n");
        tipsArea.append("2. Balanced Diet: Include fruits and vegetables in every meal\n\n");
        tipsArea.append("3. Regular Exercise: 30 minutes of walking daily\n\n");
        tipsArea.append("4. Adequate Rest: 7-8 hours of sleep per night\n\n");
        tipsArea.append("5. Hygiene: Wash hands regularly, especially before meals\n\n");
        tipsArea.append("6. Mental Health: Take breaks and practice stress management\n\n");
        tipsArea.append("7. Regular Checkups: Don't skip your health appointments\n\n");
        tipsArea.append("8. Medication: Take prescribed medicines on time\n");
        
        JScrollPane scrollPane = new JScrollPane(tipsArea);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadPersonalInfo() {
        StringBuilder info = new StringBuilder();
        info.append("PERSONAL HEALTH INFORMATION\n");
        info.append("==========================\n\n");
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) {
                profileArea.setText("Database connection failed");
                return;
            }
            
            // Get basic migrant info
            String migrantSql = "SELECT full_name, date_of_birth, contact_number, emergency_contact, medical_history, region FROM migrant_workers WHERE migrant_id = ?";
            PreparedStatement migrantStmt = conn.prepareStatement(migrantSql);
            migrantStmt.setString(1, migrantId);
            ResultSet migrantRs = migrantStmt.executeQuery();
            
            if (migrantRs.next()) {
                info.append("Migrant ID: ").append(migrantId).append("\n");
                info.append("Name: ").append(migrantRs.getString("full_name")).append("\n");
                info.append("Date of Birth: ").append(migrantRs.getString("date_of_birth")).append("\n");
                info.append("Contact: ").append(migrantRs.getString("contact_number")).append("\n");
                info.append("Emergency Contact: ").append(migrantRs.getString("emergency_contact")).append("\n");
                info.append("Region: ").append(migrantRs.getString("region")).append("\n");
                info.append("Medical History: ").append(migrantRs.getString("medical_history")).append("\n\n");
            }
            migrantStmt.close();
            
            // Get last health survey
            String surveySql = "SELECT survey_date, blood_pressure, temperature, recommendations FROM health_surveys WHERE migrant_id = ? ORDER BY survey_date DESC LIMIT 1";
            PreparedStatement surveyStmt = conn.prepareStatement(surveySql);
            surveyStmt.setString(1, migrantId);
            ResultSet surveyRs = surveyStmt.executeQuery();
            
            if (surveyRs.next()) {
                info.append("Last Health Check: ").append(surveyRs.getString("survey_date")).append("\n");
                info.append("Blood Pressure: ").append(surveyRs.getString("blood_pressure")).append("\n");
                info.append("Temperature: ").append(surveyRs.getString("temperature")).append("°C\n");
                info.append("Recommendations: ").append(surveyRs.getString("recommendations")).append("\n");
            }
            surveyStmt.close();
            
            // Get assigned health worker
            String workerSql = "SELECT worker_id FROM health_surveys WHERE migrant_id = ? ORDER BY survey_date DESC LIMIT 1";
            PreparedStatement workerStmt = conn.prepareStatement(workerSql);
            workerStmt.setString(1, migrantId);
            ResultSet workerRs = workerStmt.executeQuery();
            
            if (workerRs.next()) {
                info.append("Last Health Worker: ").append(workerRs.getString("worker_id")).append("\n");
            }
            workerStmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
            info.append("Error loading health information: ").append(e.getMessage()).append("\n");
        }
        
        profileArea.setText(info.toString());
    }
    
    private String[] loadMedicalHistoryFromDB() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return new String[]{"Database connection failed"};
            
            String sql = "SELECT visit_date, diagnosis, prescription FROM patient_records WHERE migrant_id = ? " +
                        "UNION SELECT survey_date, symptoms, recommendations FROM health_surveys WHERE migrant_id = ? " +
                        "ORDER BY 1 DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, migrantId);
            pstmt.setString(2, migrantId);
            ResultSet rs = pstmt.executeQuery();
            
            java.util.ArrayList<String> history = new java.util.ArrayList<>();
            while (rs.next()) {
                String date = rs.getString(1);
                String diagnosis = rs.getString(2);
                String treatment = rs.getString(3);
                history.add(date + ": " + diagnosis + " - " + treatment);
            }
            
            pstmt.close();
            
            if (history.isEmpty()) {
                return new String[]{"No medical history found"};
            }
            
            return history.toArray(new String[0]);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{"Error loading medical history: " + e.getMessage()};
        }
    }
    
    private Object[][] loadAppointmentsFromDB() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn == null) return new Object[][]{{"Database connection failed", "", "", "", ""}};
            
            String sql = "SELECT DATE(appointment_date), TIME(appointment_date), doctor, purpose, status FROM appointments WHERE migrant_id = ? ORDER BY appointment_date DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, migrantId);
            ResultSet rs = pstmt.executeQuery();
            
            java.util.ArrayList<Object[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getString(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                rows.add(row);
            }
            
            pstmt.close();
            
            if (rows.isEmpty()) {
                return new Object[][] {{"No appointments scheduled", "", "", "", ""}};
            }
            
            return rows.toArray(new Object[0][0]);
            
        } catch (SQLException e) {
            e.printStackTrace();
            return new Object[][] {{"Error loading appointments: " + e.getMessage(), "", "", "", ""}};
        }
    }
    
    private void handleProfileAction(String action) {
        switch (action) {
            case "Update Information":
                JOptionPane.showMessageDialog(this, "Opening information update form...");
                break;
            case "Request Appointment":
                new BookAppointmentListener().actionPerformed(null);
                break;
            case "Download Health Card":
                JOptionPane.showMessageDialog(this, "Downloading health card PDF...");
                break;
        }
    }
    
    private void refreshMedicalHistory() {
        // Reload medical history
        String[] history = loadMedicalHistoryFromDB();
        
        // Find and update the medical history panel
        JTabbedPane tabbedPane = (JTabbedPane) ((JPanel) getContentPane().getComponent(0)).getComponent(1);
        JPanel medicalHistoryPanel = (JPanel) tabbedPane.getComponentAt(1);
        
        // Remove old components and add new ones
        medicalHistoryPanel.removeAll();
        
        JLabel titleLabel = new JLabel("My Medical History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(136, 23, 152));
        
        JList<String> historyList = new JList<>(history);
        historyList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyList.setBackground(Color.WHITE);
        JScrollPane listScroll = new JScrollPane(historyList);
        
        JButton refreshButton = new JButton("Refresh History");
        refreshButton.setBackground(new Color(136, 23, 152));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> refreshMedicalHistory());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);
        
        medicalHistoryPanel.setLayout(new BorderLayout());
        medicalHistoryPanel.add(titleLabel, BorderLayout.NORTH);
        medicalHistoryPanel.add(listScroll, BorderLayout.CENTER);
        medicalHistoryPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        medicalHistoryPanel.revalidate();
        medicalHistoryPanel.repaint();
        
        JOptionPane.showMessageDialog(this, "Medical history refreshed!");
    }
    
    private void refreshAppointments() {
        // Similar implementation as refreshMedicalHistory
        Object[][] data = loadAppointmentsFromDB();
        
        JTabbedPane tabbedPane = (JTabbedPane) ((JPanel) getContentPane().getComponent(0)).getComponent(1);
        JPanel appointmentsPanel = (JPanel) tabbedPane.getComponentAt(2);
        
        appointmentsPanel.removeAll();
        
        JLabel titleLabel = new JLabel("My Appointments", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(136, 23, 152));
        
        String[] columns = {"Date", "Time", "Doctor", "Purpose", "Status"};
        JTable appointmentsTable = new JTable(data, columns);
        appointmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appointmentsTable.setRowHeight(25);
        JScrollPane tableScroll = new JScrollPane(appointmentsTable);
        
        JButton newAppointmentButton = new JButton("Book New Appointment");
        newAppointmentButton.setBackground(new Color(136, 23, 152));
        newAppointmentButton.setForeground(Color.WHITE);
        newAppointmentButton.addActionListener(new BookAppointmentListener());
        
        JButton refreshButton = new JButton("Refresh Appointments");
        refreshButton.setBackground(new Color(100, 100, 100));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> refreshAppointments());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(newAppointmentButton);
        buttonPanel.add(refreshButton);
        
        appointmentsPanel.setLayout(new BorderLayout());
        appointmentsPanel.add(titleLabel, BorderLayout.NORTH);
        appointmentsPanel.add(tableScroll, BorderLayout.CENTER);
        appointmentsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        appointmentsPanel.revalidate();
        appointmentsPanel.repaint();
        
        JOptionPane.showMessageDialog(this, "Appointments refreshed!");
    }
    
    private class BookAppointmentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a better appointment booking dialog with date validation
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            
            // Get current date for default values
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 7); // Default to one week from now
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            
            JTextField dateField = new JTextField(dateFormat.format(cal.getTime()));
            JTextField timeField = new JTextField("10:00");
            JTextField doctorField = new JTextField("Dr. Sharma");
            JComboBox<String> purposeCombo = new JComboBox<>(new String[]{
                "Routine Checkup", "Follow-up", "Consultation", "Emergency", "Vaccination", "Other"
            });
            
            panel.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
            panel.add(dateField);
            panel.add(new JLabel("Appointment Time (HH:MM):"));
            panel.add(timeField);
            panel.add(new JLabel("Doctor/Provider:"));
            panel.add(doctorField);
            panel.add(new JLabel("Purpose:"));
            panel.add(purposeCombo);
            
            int option = JOptionPane.showConfirmDialog(MigrantWorker.this,
                panel, "Book New Appointment", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                String date = dateField.getText().trim();
                String time = timeField.getText().trim();
                String doctor = doctorField.getText().trim();
                String purpose = purposeCombo.getSelectedItem().toString();
                
                // Validate inputs
                if (date.isEmpty() || time.isEmpty() || doctor.isEmpty()) {
                    JOptionPane.showMessageDialog(MigrantWorker.this,
                        "Please fill in all appointment details", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Validate date format
                if (!isValidDate(date)) {
                    JOptionPane.showMessageDialog(MigrantWorker.this,
                        "Invalid date format. Please use YYYY-MM-DD format (e.g., 2024-12-25)",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Validate time format
                if (!isValidTime(time)) {
                    JOptionPane.showMessageDialog(MigrantWorker.this,
                        "Invalid time format. Please use HH:MM format (e.g., 14:30)",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    if (conn == null) {
                        JOptionPane.showMessageDialog(MigrantWorker.this,
                            "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    String sql = "INSERT INTO appointments (migrant_id, appointment_date, doctor, purpose, status) VALUES (?, ?, ?, ?, 'Scheduled')";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, migrantId);
                    pstmt.setString(2, date + " " + time + ":00");
                    pstmt.setString(3, doctor);
                    pstmt.setString(4, purpose);
                    
                    int rowsAffected = pstmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(MigrantWorker.this,
                            "Appointment booked successfully!\n\nDate: " + date + 
                            "\nTime: " + time + 
                            "\nDoctor: " + doctor +
                            "\nPurpose: " + purpose,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Refresh appointments table
                        refreshAppointments();
                    } else {
                        JOptionPane.showMessageDialog(MigrantWorker.this,
                            "Failed to book appointment. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    pstmt.close();
                    
                } catch (SQLException ex) {
                    String errorMessage = ex.getMessage();
                    if (errorMessage.contains("Incorrect datetime value")) {
                        JOptionPane.showMessageDialog(MigrantWorker.this,
                            "Invalid date or time format. Please check your input.\n" +
                            "Date should be YYYY-MM-DD (e.g., 2024-12-25)\n" +
                            "Time should be HH:MM (e.g., 14:30)",
                            "Invalid Date/Time", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(MigrantWorker.this,
                            "Database error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    ex.printStackTrace();
                }
            }
        }
        
        private boolean isValidDate(String date) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false); // This will check for valid dates like 2024-02-30
                Date parsedDate = sdf.parse(date);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        
        private boolean isValidTime(String time) {
            return time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
        }
    }
}
