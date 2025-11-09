-- Niramaya Sample Data
-- This file contains sample data for demonstration purposes

USE niramaya_db;

-- Sample Migrant Workers
INSERT INTO migrant_workers (aadhar_number, full_name, age, gender, phone_number, native_state, current_address, occupation, password) VALUES
('1234-5678-9012', 'Rahul Kumar', 32, 'Male', '+919876543210', 'Bihar', 'Construction Site, Kochi', 'Construction Worker', 'password123'),
('9876-5432-1098', 'Priya Singh', 28, 'Female', '+919876543211', 'West Bengal', 'Domestic Worker Quarters, Thrissur', 'Domestic Worker', 'password123'),
('5555-6666-7777', 'Sanjay Patel', 35, 'Male', '+919876543212', 'Uttar Pradesh', 'Farm Labor Camp, Palakkad', 'Agricultural Worker', 'password123'),
('1111-2222-3333', 'Anita Yadav', 25, 'Female', '+919876543213', 'Jharkhand', 'Textile Factory Quarters, Kozhikode', 'Factory Worker', 'password123');

-- Sample Community Health Workers
INSERT INTO community_health_workers (employee_id, full_name, email, phone_number, region, qualifications, password) VALUES
('CHW001', 'Dr. Anjali Menon', 'anjali.menon@niramaya.org', '+919876543300', 'Ernakulam', 'MBBS, Community Medicine', 'health123'),
('CHW002', 'Dr. Rajesh Kumar', 'rajesh.kumar@niramaya.org', '+919876543301', 'Thrissur', 'BAMS, Public Health', 'health123'),
('CHW003', 'Sneha Nair', 'sneha.nair@niramaya.org', '+919876543302', 'Kozhikode', 'B.Sc Nursing, Community Health', 'health123');

-- Sample Healthcare Providers
INSERT INTO healthcare_providers (license_number, provider_name, provider_type, address, phone_number, email, password) VALUES
('HOSP001', 'Government Medical College Ernakulam', 'Hospital', 'Ernakulam, Kerala', '+914812221234', 'info@gmcekm.org', 'provider123'),
('CLIN001', 'Community Health Center Thrissur', 'Clinic', 'Thrissur, Kerala', '+914872221235', 'contact@chctsr.org', 'provider123'),
('CAMP001', 'Mobile Medical Camp Palakkad', 'Medical Camp', 'Palakkad District, Kerala', '+914872221236', 'camp@niramaya.org', 'provider123');

-- Sample Medical Records
INSERT INTO medical_records (worker_id, visit_date, diagnosis, prescription, symptoms, health_worker_id, provider_id) VALUES
(1, '2024-01-15', 'Common Cold', 'Paracetamol 500mg - 1 tab thrice daily for 3 days', 'Fever, Cough, Headache', 1, 2),
(2, '2024-01-10', 'Minor Injury', 'Antiseptic cream, Bandage', 'Cut on hand while working', 2, 3),
(1, '2024-01-20', 'Follow-up', 'Continue medication, Rest', 'Improved condition', 1, 2);

-- Sample Emergency Contacts
INSERT INTO emergency_contacts (worker_id, contact_name, relationship, phone_number, address, is_primary) VALUES
(1, 'Sunita Kumar', 'Wife', '+919876543215', 'Same as worker address', TRUE),
(1, 'Ramesh Kumar', 'Brother', '+919876543216', 'Patna, Bihar', FALSE),
(2, 'Amit Singh', 'Husband', '+919876543217', 'Kolkata, West Bengal', TRUE);

-- Verification Query
SELECT 
    (SELECT COUNT(*) FROM migrant_workers) as total_workers,
    (SELECT COUNT(*) FROM community_health_workers) as total_health_workers,
    (SELECT COUNT(*) FROM medical_records) as total_medical_records;
