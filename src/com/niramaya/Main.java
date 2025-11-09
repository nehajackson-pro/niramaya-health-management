package niramay;

import niramay.database.DatabaseConnection;
import niramay.database.DatabaseSetup;
import niramay.ui.WelcomeFrame;
import javax.swing.*;
import java.awt.*;

public class Main {
    private static final String APP_NAME = "NIRAMAYA Healthcare";
    private static final String VERSION = "1.0.0";
    
    public static void main(String[] args) {
        // Set professional look and feel
        setupProfessionalLookAndFeel();
        
        // Show splash screen
        showSplashScreen();
        
        // Initialize database
        initializeDatabase();
        
        // Launch main application
        SwingUtilities.invokeLater(() -> {
            new WelcomeFrame().setVisible(true);
        });
    }
    
    private static void setupProfessionalLookAndFeel() {
        try {
            // Try to set the system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            try {
                // Fallback to cross-platform look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Warning: Could not set any look and feel");
            }
        }
    }
    
    private static void showSplashScreen() {
        JWindow splashScreen = new JWindow();
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(0, 82, 155));
        
        JLabel titleLabel = new JLabel(APP_NAME, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 20, 10, 20));
        
        JLabel versionLabel = new JLabel("Version " + VERSION, JLabel.CENTER);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(200, 220, 240));
        
        JLabel loadingLabel = new JLabel("Initializing...", JLabel.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 30, 20));
        
        content.add(titleLabel, BorderLayout.CENTER);
        content.add(versionLabel, BorderLayout.NORTH);
        content.add(loadingLabel, BorderLayout.SOUTH);
        
        splashScreen.setContentPane(content);
        splashScreen.setSize(300, 200);
        splashScreen.setLocationRelativeTo(null);
        splashScreen.setVisible(true);
        
        // Simulate loading
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        splashScreen.dispose();
    }
    
    private static void initializeDatabase() {
        System.out.println("🔍 Checking database setup...");
        
        if (!DatabaseConnection.testConnection()) {
            System.err.println("❌ Database connection failed!");
            int option = JOptionPane.showConfirmDialog(null,
                "Database connection failed. Would you like to run setup?",
                "Database Setup",
                JOptionPane.YES_NO_OPTION);
                
            if (option == JOptionPane.YES_OPTION) {
                DatabaseSetup.setupDatabase();
            }
        } else {
            System.out.println("✅ Database connection successful!");
            
            if (!DatabaseSetup.isDatabaseSetUp()) {
                System.out.println("⚠️ Database tables missing, running setup...");
                DatabaseSetup.setupDatabase();
            }
        }
    }
}
