# J.A.V.A (Just a Volunteer Away)

A community-driven platform that connects volunteers with elderly individuals who need help with daily tasks.
Our mission: make volunteering more accessible and ensure elderly users receive reliable support.

## Features

- **User Management**: Profiles for both elderly users and volunteers.
- **Task Management**: Schedule, assign, and track tasks easily.
- **Volunteer Matching**: Smartly match volunteers with tasks based on availability.
- **Administrative Tools**: User supervision, reporting, and monitoring.
- **Task Requests**: Elderly users can create requests for assistance.
- **Volunteer Actions**: Volunteers can view, accept, and complete tasks.
- **Leaderboard System**: Track volunteer engagement with points and rankings.
- **Task Reassignment**: Elderly users can remove volunteers and reassign tasks.
- **Reports & Analytics**: Admin dashboards for oversight.
- **Donations**: Support the platform's maintenance and growth.

## Project Structure

The project follows a multi-layer architecture:

- **Model Layer**: Entity classes representing database tables
- **DAO Layer**: Data access objects for database operations
- **Service Layer**: Business logic implementation
- **UI Layer**: Console-based user interface

## Getting Started

### Prerequisites

- Java 8 or higher (project is configured for Java 8)
- MySQL Database (version 5.7 or higher recommended)
- Maven 3.6+ for building the project

### Database Setup

1. **Setting up the MySQL database:**

   You have two options for setting up the database:

   **Option 1: Using the provided SQL scripts**
   ```sh
   # Log into MySQL as root or an admin user
   mysql -u root -p
   
   # Run the database user creation script
   source create_db_user.sql
   
   # Run the database setup script
   source setup_database.sql
   
   # Exit MySQL
   exit
   ```

   **Option 2: Manual setup**
   ```sql
   -- Create the database
   CREATE DATABASE IF NOT EXISTS volunteer_app;
   
   -- Create a database user (recommended for security)
   CREATE USER IF NOT EXISTS 'volunteer_user'@'localhost' IDENTIFIED BY 'volunteer_pass';
   GRANT ALL PRIVILEGES ON volunteer_app.* TO 'volunteer_user'@'localhost';
   FLUSH PRIVILEGES;
   
   -- Use the database
   USE volunteer_app;
   
   -- Import the schema from the setup_database.sql file
   ```

   **Option 3: Terminal commands (recommended for scripts/automation)**
   ```sh
   # Create database and user (will prompt for MySQL root password)
   mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS volunteer_app; \
   CREATE USER IF NOT EXISTS 'volunteer_user'@'localhost' IDENTIFIED BY 'volunteer_pass'; \
   GRANT ALL PRIVILEGES ON volunteer_app.* TO 'volunteer_user'@'localhost'; \
   FLUSH PRIVILEGES;"
   
   # Import the schema directly from the terminal
   mysql -u volunteer_user -p'volunteer_pass' volunteer_app < src/main/resources/sql/schema.sql
   
   # Verify the tables were created
   mysql -u volunteer_user -p'volunteer_pass' volunteer_app -e "SHOW TABLES;"
   
   # Optional: Check the content of a specific table
   mysql -u volunteer_user -p'volunteer_pass' volunteer_app -e "SELECT * FROM users;"
   ```

2. **Configure database connection:**

   Edit `src/main/resources/config/database.properties` with your MySQL credentials:
   ```properties
   # Database Configuration
   db.url=jdbc:mysql://localhost:3306/volunteer_app?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   db.username=volunteer_user
   db.password=volunteer_pass
   db.driver=com.mysql.cj.jdbc.Driver
   db.connectionPoolSize=10
   ```

### Building and Running the Application

1. **Clone the repository:**

   ```sh
   git clone https://github.com/SamSunny4/J.A.V.A-Just-A-Volunteer-Away.git
   ```

2. **Navigate to the project folder:**

   ```sh
   cd J.A.V.A-Just-A-Volunteer-Away
   ```

3. **Build the application:**

   ```sh
   mvn clean package
   ```
   This will create two JAR files in the `target` directory:
   - `volunteer-away-1.0-SNAPSHOT.jar` (without dependencies)
   - `volunteer-away-1.0-SNAPSHOT-jar-with-dependencies.jar` (with all dependencies bundled)

4. **Run the application:**

   ```sh
   java -jar target/volunteer-away-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

5. **Using the Command-Line Interface:**

   Once the application starts, you'll see a menu with the following options:
   
   - **Login**: Log in with an existing user account
   - **Register**: Create a new user account (either as elderly or volunteer)
   - **About**: Display information about the application
   - **Exit**: Close the application
   
   After logging in, you'll have access to different functionality depending on your role:
   
   **For Elderly Users:**
   - Create new task requests
   - View your requested tasks
   - Update your profile information
   
   **For Volunteers:**
   - View available tasks and accept them
   - View your assigned tasks
   - Update task status (e.g., mark as in progress or completed)
   - Update your profile information

## Demo Accounts

The application comes with demo accounts for testing:

- **Admin**: Username: `admin`, Password: `admin`
- **Elderly**: Username: `elderly1`, Password: `password`
- **Volunteer**: Username: `volunteer1`, Password: `password`

## Common SQL Operations

Here are some useful SQL commands for managing the database:

### Viewing Data

```sql
-- View all users
SELECT * FROM users;

-- View all elderly users
SELECT * FROM users WHERE role = 'ELDERLY';

-- View all volunteer users
SELECT * FROM users WHERE role = 'VOLUNTEER';

-- View all available tasks
SELECT * FROM tasks WHERE status = 'AVAILABLE';

-- View tasks assigned to a specific volunteer (replace volunteer_id with actual ID)
SELECT * FROM tasks WHERE volunteer_id = [volunteer_id];
```

### Managing Tasks

```sql
-- Manually update a task's status (replace task_id with actual ID)
UPDATE tasks SET status = 'COMPLETED' WHERE task_id = [task_id];

-- Delete a task (replace task_id with actual ID)
DELETE FROM tasks WHERE task_id = [task_id];

-- Reset all tasks to available status (useful for testing)
UPDATE tasks SET status = 'AVAILABLE', volunteer_id = NULL;
```

### Database Maintenance

```sql
-- Backup the database
mysqldump -u [username] -p volunteer_app > volunteer_app_backup.sql

-- Restore the database from backup
mysql -u [username] -p volunteer_app < volunteer_app_backup.sql

-- Reset auto-increment counters (if needed)
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE tasks AUTO_INCREMENT = 1;
```

## Troubleshooting

### Common Issues and Solutions

1. **Database Connection Problems**
   - Verify MySQL is running: `service mysql status` (Linux) or check MySQL in Services (Windows)
   - Ensure database credentials in `database.properties` are correct
   - Check if firewall is blocking the connection (usually port 3306)

2. **Application Startup Issues**
   - Check for Java version compatibility: `java -version` (should be Java 8 or higher)
   - Verify all dependencies are included in the JAR file
   - Look for detailed error messages in the console output

3. **Runtime Errors**
   - Database schema issues: ensure tables are created correctly
   - Permission problems: verify database user has proper privileges
   - Check application logs for detailed error information

## Key Features Implementation

### Leaderboard System

- Volunteers earn points for completing tasks
- Points contribute to volunteer rankings and levels
- Leaderboard displays top volunteers with stats

### Task Reassignment

- Elderly users can remove volunteers from assigned tasks
- Tasks are automatically returned to the available pool
- System tracks reassignment history and reasons

## Contributing

We welcome contributions! To get started:

1. Fork the repository
2. Create a feature branch:
   ```sh
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```sh
   git commit -m "Add feature"
   ```
4. Push to your fork and submit a Pull Request

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Vision

J.A.V.A (Just a Volunteer Away) bridges the gap between those in need and those willing to help.
Together, we can build stronger, more supportive communities.