-- Niramaya Health Management System Database Schema
-- MBCET Hackathon Project

CREATE DATABASE IF NOT EXISTS niramaya_db;
USE niramaya_db;

-- Migrant Workers Table
CREATE TABLE migrant_workers (
    worker_id INT AUTO_INCREMENT PRIMARY KEY,
    aadhar_number VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    age INT,
    gender ENUM('Male', 'Female', 'Other'),
    phone_number VARCHAR(15),
    native_state VARCHAR(50),
    current_address TEXT,
    occupation VARCHAR(100),
    password VARCHAR(255),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Community Health Workers Table
CREATE TABLE community_health_workers (
    health_worker_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(15),
    region VARCHAR(50),
    qualifications TEXT,
    password VARCHAR(255),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Healthcare Providers Table
CREATE TABLE healthcare_providers (
    provider_id INT AUTO_INCREMENT PRIMARY KEY,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    provider_name VARCHAR(100) NOT NULL,
    provider_type ENUM('Hospital', 'Clinic', 'Medical Camp'),
    address TEXT,
    phone_number VARCHAR(15),
    email VARCHAR(100),
    password VARCHAR(255),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Medical Records Table
CREATE TABLE medical_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    worker_id INT,
    visit_date DATE NOT NULL,
    diagnosis TEXT,
    prescription TEXT,
    symptoms TEXT,
    doctor_notes TEXT,
    health_worker_id INT,
    provider_id INT,
    next_visit_date DATE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (worker_id) REFERENCES migrant_workers(worker_id),
    FOREIGN KEY (health_worker_id) REFERENCES community_health_workers(health_worker_id),
    FOREIGN KEY (provider_id) REFERENCES healthcare_providers(provider_id)
);

-- Emergency Contacts Table
CREATE TABLE emergency_contacts (
    contact_id INT AUTO_INCREMENT PRIMARY KEY,
    worker_id INT,
    contact_name VARCHAR(100) NOT NULL,
    relationship VARCHAR(50),
    phone_number VARCHAR(15) NOT NULL,
    address TEXT,
    is_primary BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (worker_id) REFERENCES migrant_workers(worker_id)
);

-- Sample Data (Optional - for demonstration)
INSERT INTO migrant_workers (aadhar_number, full_name, age, gender, phone_number, native_state, occupation, password) 
VALUES 
('1234-5678-9012', 'Rahul Kumar', 32, 'Male', '+919876543210', 'Bihar', 'Construction Worker', 'password123'),
('9876-5432-1098', 'Priya Singh', 28, 'Female', '+919876543211', 'West Bengal', 'Domestic Worker', 'password123');

INSERT INTO community_health_workers (employee_id, full_name, email, phone_number, region, qualifications, password) 
VALUES 
('CHW001', 'Dr. Anjali Menon', 'anjali.menon@niramaya.org', '+919876543300', 'Ernakulam', 'MBBS, Community Medicine', 'health123');

-- Display creation confirmation
SELECT 'Niramaya Database Schema Created Successfully!' as status;
