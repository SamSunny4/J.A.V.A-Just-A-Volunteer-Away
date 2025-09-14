-- J.A.V.A (Just a Volunteer Away) Database Schema

-- Drop tables if they exist to avoid conflicts during initialization
DROP TABLE IF EXISTS donations;
DROP TABLE IF EXISTS task_history;
DROP TABLE IF EXISTS user_points;
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS admin_users;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_roles;

-- User roles table
CREATE TABLE user_roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

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
    role_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    bio TEXT,
    profile_image VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES user_roles(role_id)
);

-- Admin users table (separate from regular users for enhanced security)
CREATE TABLE admin_users (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- User points table for gamification and leaderboard
CREATE TABLE user_points (
    point_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    points INT DEFAULT 0,
    level INT DEFAULT 1,
    rank VARCHAR(50) DEFAULT 'Newcomer',
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
    status ENUM('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'AVAILABLE', 'REASSIGNED') DEFAULT 'AVAILABLE',
    location TEXT,
    scheduled_date DATE NOT NULL,
    scheduled_time TIME NOT NULL,
    estimated_duration INT NOT NULL, -- in minutes
    urgency_level ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
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
    changed_by ENUM('VOLUNTEER', 'ELDERLY', 'ADMIN'),
    previous_status ENUM('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'AVAILABLE', 'REASSIGNED'),
    new_status ENUM('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'AVAILABLE', 'REASSIGNED'),
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

-- Insert initial roles
INSERT INTO user_roles (role_name) VALUES 
('ADMIN'),
('ELDERLY'),
('VOLUNTEER');

-- Create an admin user (password: admin123)
INSERT INTO users (username, password, email, first_name, last_name, role_id) 
VALUES ('admin', 'hashed_password_here', 'admin@java.com', 'Admin', 'User', 1);
