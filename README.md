# niramaya-health-management
Java NetBeans Apache Health Record Management System for Migrant Workers in Kerala - MBCET Hackathon Project
# 🏥 Niramaya - Health Management System

## 🌟 Project Overview

**Niramaya** addresses critical healthcare gaps faced by migrant workers in Kerala by providing a digital health record management system. This backend API supports multi-language access, emergency medical features, and secure data management.

### 🎯 Problem Statement
- Migrant workers lack organized health records
- Language barriers in healthcare access
- No centralized medical history system
- Emergency contact management challenges

### 💡 Our Solution
- Digital health record management
- Multi-language support framework
- Emergency medical features
- Secure and scalable REST APIs

## 🛠 Tech Stack

- **Backend Framework:** Java, Spring Boot
- **Database:** MySQL
- **Security:** Spring Security, JWT
- **API Documentation:** Swagger/OpenAPI
- **Build Tool:** Maven
- **Testing:** JUnit, Mockito

## 📋 API Endpoints

### Patient Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/patients` | Get all patients |
| GET | `/api/patients/{id}` | Get patient by ID |
| POST | `/api/patients` | Create new patient |
| PUT | `/api/patients/{id}` | Update patient |
| GET | `/api/patients/search?keyword={}` | Search patients |

### Medical Records
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/records/patient/{patientId}` | Get patient medical history |
| POST | `/api/records` | Add medical record |
| GET | `/api/records/emergency/{patientId}` | Get emergency medical info |

## 🗃 Database Schema

### Core Tables
- `patients` - Patient demographics and information
- `medical_records` - Health history and treatments  
- `emergency_contacts` - Emergency contact details
- `appointments` - Medical appointment scheduling

### Sample Entity Relationship
```sql
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aadhar_number VARCHAR(20) UNIQUE,
    name VARCHAR(100) NOT NULL,
    age INT,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    phone_number VARCHAR(15),
    native_state VARCHAR(50),
    current_address TEXT,
    occupation VARCHAR(100),
    medical_history TEXT,
    registration_date DATE
);
