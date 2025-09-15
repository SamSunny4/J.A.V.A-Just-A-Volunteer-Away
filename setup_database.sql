CREATE DATABASE IF NOT EXISTS volunteer_app;
USE volunteer_app;

-- J.A.V.A (Just a Volunteer Away) Database Schema

-- Drop tables if they exist to avoid conflicts during initialization
DROP TABLE IF EXISTS donations;
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
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    bio TEXT,
    profile_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- User points table for gamification and leaderboard
CREATE TABLE user_points (
    point_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    points INT DEFAULT 0,
    level INT DEFAULT 1,
    user_rank VARCHAR(50) DEFAULT 'Newcomer',
    tasks_completed INT DEFAULT 0,
    tasks_cancelled INT DEFAULT 0,
    tasks_reassigned INT DEFAULT 0,
    hours_volunteered INT DEFAULT 0,
    streak_days INT DEFAULT 0,
    last_activity_date DATE,
    leaderboard_position INT,
    badges TEXT,
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
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    location TEXT,
    scheduled_date DATE NOT NULL,
    scheduled_time TIME NOT NULL,
    estimated_duration INT NOT NULL, -- in minutes
    urgency_level VARCHAR(20) DEFAULT 'MEDIUM',
    previous_volunteer_id INT,
    reassignment_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_id) REFERENCES users(user_id),
    FOREIGN KEY (volunteer_id) REFERENCES users(user_id),
    FOREIGN KEY (previous_volunteer_id) REFERENCES users(user_id)
);

-- Task history table for tracking changes
CREATE TABLE task_history (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    task_id INT NOT NULL,
    changed_by_id INT NOT NULL,
    changed_by VARCHAR(20),
    previous_status VARCHAR(20),
    new_status VARCHAR(20),
    previous_volunteer_id INT,
    new_volunteer_id INT,
    reassignment_reason TEXT,
    notes TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id),
    FOREIGN KEY (changed_by_id) REFERENCES users(user_id),
    FOREIGN KEY (previous_volunteer_id) REFERENCES users(user_id),
    FOREIGN KEY (new_volunteer_id) REFERENCES users(user_id)
);

-- Donations table
CREATE TABLE donations (
    donation_id INT PRIMARY KEY AUTO_INCREMENT,
    donor_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    donation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    is_anonymous BOOLEAN DEFAULT FALSE,
    message TEXT,
    FOREIGN KEY (donor_id) REFERENCES users(user_id)
);

-- Insert initial data for testing

-- Create an admin user (password: admin123 - will be hashed in the application)
INSERT INTO users (username, password, email, first_name, last_name, role) 
VALUES ('admin', '$2a$10$XOPEUldsjJlDaD9UOZ1.sOIlwTPiaq5KRXsnoHr9bKIvTnMaBVLpW', 'admin@java.com', 'Admin', 'User', 'ADMIN');

-- Create sample elderly user (password: password - will be hashed in the application)
INSERT INTO users (username, password, email, first_name, last_name, phone_number, role, date_of_birth) 
VALUES ('elderly1', '$2a$10$XZ2YEW8OolNQvP0JdNYYUOIRS7SxwjV3xXdEfRmiGIVvP3daL0VQS', 'elderly1@example.com', 'John', 'Smith', '555-1234', 'ELDERLY', '1950-05-15');

-- Create sample volunteer user (password: password - will be hashed in the application)
INSERT INTO users (username, password, email, first_name, last_name, phone_number, role, date_of_birth) 
VALUES ('volunteer1', '$2a$10$XZ2YEW8OolNQvP0JdNYYUOIRS7SxwjV3xXdEfRmiGIVvP3daL0VQS', 'volunteer1@example.com', 'Jane', 'Doe', '555-5678', 'VOLUNTEER', '1995-08-22');

-- Create user points for the volunteer
INSERT INTO user_points (user_id, points, level, user_rank, tasks_completed, hours_volunteered, streak_days)
VALUES (3, 250, 2, 'Regular Volunteer', 5, 8, 3);

-- Create a sample available task
INSERT INTO tasks (title, description, requester_id, status, location, scheduled_date, scheduled_time, estimated_duration, urgency_level)
VALUES ('Help with grocery shopping', 'Need help with weekly grocery shopping. I cannot carry heavy items.', 2, 'AVAILABLE', '123 Main St, Anytown', CURDATE() + INTERVAL 2 DAY, '10:00', 60, 'MEDIUM');

-- Create a sample assigned task
INSERT INTO tasks (title, description, requester_id, volunteer_id, status, location, scheduled_date, scheduled_time, estimated_duration, urgency_level)
VALUES ('Help with yard work', 'Need help with mowing the lawn and trimming bushes.', 2, 3, 'ASSIGNED', '123 Main St, Anytown', CURDATE() + INTERVAL 5 DAY, '14:00', 120, 'LOW');