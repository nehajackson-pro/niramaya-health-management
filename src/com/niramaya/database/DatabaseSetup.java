package niramay.database;

import java.sql.*;

public class DatabaseSetup {
    
    public static void main(String[] args) {
        setupDatabase();
    }
    
    public static void setupDatabase() {
        Connection conn = null;
        Statement stmt = null;
        
        try {
            // First connect without database to create it
            String baseUrl = "jdbc:mysql://localhost:3306/";
            conn = DriverManager.getConnection(baseUrl, "root", "root123");
            stmt = conn.createStatement();
            
            // Create database if it doesn't exist
            String createDBSQL = "CREATE DATABASE IF NOT EXISTS niramay_healthcare";
            stmt.executeUpdate(createDBSQL);
            System.out.println("✅ Database 'niramay_healthcare' created or already exists");
            
            // Close and reconnect to the specific database
            stmt.close();
            conn.close();
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/niramay_healthcare", "root", "root123");
            stmt = conn.createStatement();
            
            // Drop tables if they exist (to start fresh)
            dropTablesIfExist(stmt);
            
            // Create tables
            createTables(stmt);
            
            // Insert sample data
            insertSampleData(stmt);
            
            System.out.println("🎉 Database setup completed successfully!");
            System.out.println("📋 Sample credentials created:");
            System.out.println("   Health Care Provider: HOSP001 / password123");
            System.out.println("   Community Health Worker: CHW001 / password123");
            System.out.println("   Migrant Worker: MW001 / password123");
            System.out.println("   Migrant Worker: MW002 / password123");
                
        } catch (SQLException e) {
            System.err.println("❌ Database setup failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void dropTablesIfExist(Statement stmt) throws SQLException {
        String[] tables = {
            "appointments", "health_surveys", "patient_records", 
            "migrant_workers", "community_health_workers", "health_care_providers"
        };
        
        for (String table : tables) {
            try {
                stmt.executeUpdate("DROP TABLE IF EXISTS " + table);
                System.out.println("✅ Dropped table: " + table);
            } catch (SQLException e) {
                System.out.println("⚠️ Could not drop table " + table + ": " + e.getMessage());
            }
        }
    }
    
    private static void createTables(Statement stmt) throws SQLException {
        // Health Care Providers Table
        String createProvidersTable = 
            "CREATE TABLE health_care_providers (" +
            "    hospital_code VARCHAR(20) PRIMARY KEY," +
            "    hospital_name VARCHAR(100) NOT NULL," +
            "    password VARCHAR(255) NOT NULL," +
            "    address TEXT," +
            "    contact_number VARCHAR(15)," +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";
        stmt.executeUpdate(createProvidersTable);
        System.out.println("✅ Table 'health_care_providers' created");
        
        // Community Health Workers Table
        String createWorkersTable = 
            "CREATE TABLE community_health_workers (" +
            "    worker_id VARCHAR(20) PRIMARY KEY," +
            "    full_name VARCHAR(100) NOT NULL," +
            "    password VARCHAR(255) NOT NULL," +
            "    region VARCHAR(50)," +
            "    contact_number VARCHAR(15)," +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";
        stmt.executeUpdate(createWorkersTable);
        System.out.println("✅ Table 'community_health_workers' created");
        
        // Migrant Workers Table with region
        String createMigrantsTable = 
            "CREATE TABLE migrant_workers (" +
            "    migrant_id VARCHAR(20) PRIMARY KEY," +
            "    full_name VARCHAR(100) NOT NULL," +
            "    password VARCHAR(255) NOT NULL," +
            "    date_of_birth DATE," +
            "    contact_number VARCHAR(15)," +
            "    emergency_contact VARCHAR(15)," +
            "    medical_history TEXT," +
            "    region VARCHAR(50)," +  // ADDED REGION FIELD
            "    assigned_worker_id VARCHAR(20)," + // ADDED ASSIGNED WORKER
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    FOREIGN KEY (assigned_worker_id) REFERENCES community_health_workers(worker_id) ON DELETE SET NULL" +
            ")";
        stmt.executeUpdate(createMigrantsTable);
        System.out.println("✅ Table 'migrant_workers' created");
        
        // Patient Records Table
        String createPatientRecordsTable = 
            "CREATE TABLE patient_records (" +
            "    record_id INT AUTO_INCREMENT PRIMARY KEY," +
            "    migrant_id VARCHAR(20)," +
            "    hospital_code VARCHAR(20)," +
            "    visit_date DATE," +
            "    diagnosis TEXT," +
            "    prescription TEXT," +
            "    notes TEXT," +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    FOREIGN KEY (migrant_id) REFERENCES migrant_workers(migrant_id) ON DELETE CASCADE," +
            "    FOREIGN KEY (hospital_code) REFERENCES health_care_providers(hospital_code) ON DELETE CASCADE" +
            ")";
        stmt.executeUpdate(createPatientRecordsTable);
        System.out.println("✅ Table 'patient_records' created");
        
        // Health Surveys Table
        String createHealthSurveysTable = 
            "CREATE TABLE health_surveys (" +
            "    survey_id INT AUTO_INCREMENT PRIMARY KEY," +
            "    migrant_id VARCHAR(20)," +
            "    worker_id VARCHAR(20)," +
            "    survey_date DATE," +
            "    blood_pressure VARCHAR(20)," +
            "    temperature DECIMAL(4,2)," +
            "    symptoms TEXT," +
            "    recommendations TEXT," +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    FOREIGN KEY (migrant_id) REFERENCES migrant_workers(migrant_id) ON DELETE CASCADE," +
            "    FOREIGN KEY (worker_id) REFERENCES community_health_workers(worker_id) ON DELETE CASCADE" +
            ")";
        stmt.executeUpdate(createHealthSurveysTable);
        System.out.println("✅ Table 'health_surveys' created");
        
        // Appointments Table
        String createAppointmentsTable = 
            "CREATE TABLE appointments (" +
            "    appointment_id INT AUTO_INCREMENT PRIMARY KEY," +
            "    migrant_id VARCHAR(20)," +
            "    appointment_date DATETIME," +
            "    doctor VARCHAR(100)," +
            "    purpose VARCHAR(200)," +
            "    status VARCHAR(20) DEFAULT 'Scheduled'," +
            "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    FOREIGN KEY (migrant_id) REFERENCES migrant_workers(migrant_id) ON DELETE CASCADE" +
            ")";
        stmt.executeUpdate(createAppointmentsTable);
        System.out.println("✅ Table 'appointments' created");
    }
    
    private static void insertSampleData(Statement stmt) throws SQLException {
        System.out.println("🗃️ Inserting sample data...");
        
        // Insert Health Care Providers
        String insertProviders = 
            "INSERT INTO health_care_providers (hospital_code, hospital_name, password, address, contact_number) VALUES " +
            "('HOSP001', 'City General Hospital', 'password123', '123 Main Street, City', '9876543210'), " +
            "('HOSP002', 'Community Health Center', 'password123', '456 Park Avenue, Town', '9876543211')";
        stmt.executeUpdate(insertProviders);
        System.out.println("✅ Inserted health care providers");
        
        // Insert Community Health Workers
        String insertWorkers = 
            "INSERT INTO community_health_workers (worker_id, full_name, password, region, contact_number) VALUES " +
            "('CHW001', 'Priya Sharma', 'password123', 'North Region', '9876543212'), " +
            "('CHW002', 'Raj Kumar', 'password123', 'South Region', '9876543213')";
        stmt.executeUpdate(insertWorkers);
        System.out.println("✅ Inserted community health workers");
        
        // Insert Migrant Workers with regions
        String insertMigrants = 
            "INSERT INTO migrant_workers (migrant_id, full_name, password, date_of_birth, contact_number, emergency_contact, medical_history, region, assigned_worker_id) VALUES " +
            "('MW001', 'Amit Singh', 'password123', '1990-05-15', '9876543214', '9876543215', 'No significant history', 'North Region', 'CHW001'), " +
            "('MW002', 'Sunita Patel', 'password123', '1985-08-22', '9876543216', '9876543217', 'Hypertension, on medication', 'South Region', 'CHW002'), " +
            "('MW003', 'Rajesh Kumar', 'password123', '1988-11-10', '9876543218', '9876543219', 'Diabetes, controlled', 'North Region', 'CHW001'), " +
            "('MW004', 'Priya Sharma', 'password123', '1992-03-25', '9876543220', '9876543221', 'Asthma', 'South Region', 'CHW002')";
        stmt.executeUpdate(insertMigrants);
        System.out.println("✅ Inserted migrant workers with region assignments");
        
        // Insert sample patient records
        String insertPatientRecords = 
            "INSERT INTO patient_records (migrant_id, hospital_code, visit_date, diagnosis, prescription, notes) VALUES " +
            "('MW001', 'HOSP001', '2024-01-15', 'Common Cold', 'Rest and fluids', 'Patient recovering well'), " +
            "('MW002', 'HOSP002', '2024-02-20', 'Hypertension Follow-up', 'Continue medication', 'Blood pressure under control')";
        stmt.executeUpdate(insertPatientRecords);
        System.out.println("✅ Inserted sample patient records");
        
        // Insert sample health surveys
        String insertSurveys = 
            "INSERT INTO health_surveys (migrant_id, worker_id, survey_date, blood_pressure, temperature, symptoms, recommendations) VALUES " +
            "('MW001', 'CHW001', '2024-03-15', '120/80', 36.8, 'None', 'Good health, continue current routine'), " +
            "('MW002', 'CHW002', '2024-03-10', '130/85', 36.9, 'Mild headache', 'Monitor blood pressure regularly'), " +
            "('MW003', 'CHW001', '2024-03-12', '118/78', 36.7, 'Fatigue', 'Get adequate rest'), " +
            "('MW004', 'CHW002', '2024-03-08', '125/82', 36.6, 'Cough', 'Drink warm fluids')";
        stmt.executeUpdate(insertSurveys);
        System.out.println("✅ Inserted sample health surveys");
        
        // Insert sample appointments
        String insertAppointments = 
            "INSERT INTO appointments (migrant_id, appointment_date, doctor, purpose, status) VALUES " +
            "('MW001', '2024-04-15 10:00:00', 'Dr. Sharma', 'Routine Checkup', 'Scheduled'), " +
            "('MW002', '2024-03-20 14:30:00', 'Dr. Patel', 'Follow-up', 'Completed'), " +
            "('MW003', '2024-04-20 11:00:00', 'Dr. Gupta', 'Consultation', 'Scheduled'), " +
            "('MW004', '2024-03-25 09:30:00', 'Dr. Reddy', 'Vaccination', 'Completed')";
        stmt.executeUpdate(insertAppointments);
        System.out.println("✅ Inserted sample appointments");
        
        System.out.println("🎉 All sample data inserted successfully!");
    }
    
    public static boolean isDatabaseSetUp() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String checkTables = 
                "SELECT " +
                "(SELECT COUNT(*) FROM health_care_providers) as providers, " +
                "(SELECT COUNT(*) FROM community_health_workers) as workers, " +
                "(SELECT COUNT(*) FROM migrant_workers) as migrants";
            
            ResultSet rs = stmt.executeQuery(checkTables);
            if (rs.next()) {
                int providers = rs.getInt("providers");
                int workers = rs.getInt("workers");
                int migrants = rs.getInt("migrants");
                
                System.out.println("📊 Database check:");
                System.out.println("   • Health care providers: " + providers);
                System.out.println("   • Community health workers: " + workers);
                System.out.println("   • Migrant workers: " + migrants);
                
                return providers > 0 && workers > 0 && migrants > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error checking database setup: " + e.getMessage());
        }
        return false;
    }
}
