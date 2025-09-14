CREATE DATABASE IF NOT EXISTS volunteer_app;

-- Create user and grant privileges
CREATE USER IF NOT EXISTS 'volunteer_user'@'localhost' IDENTIFIED BY 'volunteer_pass';
GRANT ALL PRIVILEGES ON volunteer_app.* TO 'volunteer_user'@'localhost';
FLUSH PRIVILEGES;

USE volunteer_app;