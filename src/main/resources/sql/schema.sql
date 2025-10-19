-- J.A.V.A (Just a Volunteer Away) Database Schema
-- MySQL Database Setup Script

-- Create database
CREATE DATABASE IF NOT EXISTS volunteer_app;
USE volunteer_app;

-- Create database user (if running this as root)
CREATE USER IF NOT EXISTS 'volunteer_user'@'localhost' IDENTIFIED BY 'volunteer_pass';
GRANT ALL PRIVILEGES ON volunteer_app.* TO 'volunteer_user'@'localhost';
FLUSH PRIVILEGES;

-- Drop tables if they exist (in correct order to avoid foreign key conflicts)
DROP TABLE IF EXISTS task_history;
DROP TABLE IF EXISTS user_points;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15),
    address TEXT,
    date_of_birth DATE,
    role VARCHAR(20) NOT NULL, -- 'ELDERLY', 'VOLUNTEER', or 'ADMIN'
    is_active BOOLEAN DEFAULT TRUE,
    bio TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- User points table for leaderboard
CREATE TABLE user_points (
    point_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    points INT DEFAULT 0,
    level INT DEFAULT 1,
    user_rank VARCHAR(50) DEFAULT 'Newcomer',
    tasks_completed INT DEFAULT 0,
    tasks_cancelled INT DEFAULT 0,
    hours_volunteered INT DEFAULT 0,
    last_activity_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Tasks table
CREATE TABLE tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    requester_id INT NOT NULL,
    volunteer_id INT,
    status VARCHAR(30) DEFAULT 'AVAILABLE', -- 'AVAILABLE', 'ASSIGNED', 'IN_PROGRESS', 'PENDING_ELDERLY_CONFIRMATION', 'PENDING_VOLUNTEER_CONFIRMATION', 'COMPLETED', 'CANCELLED'
    location TEXT,
    scheduled_date DATE NOT NULL,
    scheduled_time TIME NOT NULL,
    estimated_duration INT NOT NULL, -- in minutes
    urgency_level VARCHAR(10) DEFAULT 'MEDIUM', -- 'LOW', 'MEDIUM', 'HIGH'
    volunteer_confirmed BOOLEAN DEFAULT FALSE, -- Volunteer marked task as completed
    elderly_confirmed BOOLEAN DEFAULT FALSE, -- Elderly marked task as completed
    previous_volunteer_id INT,
    reassignment_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (volunteer_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (previous_volunteer_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Task history table for tracking changes
CREATE TABLE task_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    task_id INT NOT NULL,
    changed_by_id INT NOT NULL,
    action_type VARCHAR(50), -- 'CREATED', 'ASSIGNED', 'REASSIGNED', 'COMPLETED', 'CANCELLED'
    previous_status VARCHAR(30),
    new_status VARCHAR(30),
    previous_volunteer_id INT,
    new_volunteer_id INT,
    reassignment_reason TEXT,
    notes TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE,
    FOREIGN KEY (changed_by_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (previous_volunteer_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (new_volunteer_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Insert sample data for testing

-- Sample elderly user (password: password123)
INSERT INTO users (username, password, email, first_name, last_name, phone_number, role, date_of_birth) 
VALUES ('john_doe', 'password123', 'john.doe@email.com', 'John', 'Doe', '555-1234', 'ELDERLY', '1950-05-15');

-- Sample volunteer users (password: password123)
INSERT INTO users (username, password, email, first_name, last_name, phone_number, role, date_of_birth) 
VALUES 
('jane_smith', 'password123', 'jane.smith@email.com', 'Jane', 'Smith', '555-5678', 'VOLUNTEER', '1995-08-22'),
('bob_volunteer', 'password123', 'bob.volunteer@email.com', 'Bob', 'Johnson', '555-9999', 'VOLUNTEER', '1992-03-10');

-- Sample admin user (password: admin123)
INSERT INTO users (username, password, email, first_name, last_name, phone_number, role, date_of_birth) 
VALUES ('admin', 'admin123', 'admin@volunteer.com', 'System', 'Administrator', '555-0000', 'ADMIN', '1990-01-01');

-- Create user points for volunteers
INSERT INTO user_points (user_id, points, level, user_rank, tasks_completed, hours_volunteered)
VALUES 
(2, 150, 2, 'Helper', 3, 5),
(3, 250, 3, 'Active Volunteer', 5, 8);

-- Sample tasks
INSERT INTO tasks (title, description, requester_id, status, location, scheduled_date, scheduled_time, estimated_duration, urgency_level)
VALUES 
('Grocery Shopping', 'Need help with weekly grocery shopping', 1, 'AVAILABLE', '123 Main St', CURDATE() + INTERVAL 2 DAY, '10:00', 60, 'MEDIUM'),
('Doctor Appointment', 'Need ride to doctor appointment', 1, 'AVAILABLE', '456 Oak Ave', CURDATE() + INTERVAL 3 DAY, '14:00', 90, 'HIGH'),
('Yard Work', 'Help with mowing the lawn', 1, 'ASSIGNED', '123 Main St', CURDATE() + INTERVAL 5 DAY, '09:00', 120, 'LOW');

-- Assign one task to a volunteer
UPDATE tasks SET volunteer_id = 2, status = 'ASSIGNED' WHERE task_id = 3;

-- Create indexes for better performance
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_volunteer ON tasks(volunteer_id);
CREATE INDEX idx_tasks_requester ON tasks(requester_id);
CREATE INDEX idx_users_role ON users(role);

-- Display success message
SELECT 'Database setup completed successfully!' AS Status;
SELECT 'Sample users created: john_doe (elderly), jane_smith & bob_volunteer (volunteers), admin (admin)' AS Info;
SELECT 'Default passwords - Users: password123, Admin: admin123' AS Credentials;