# J.A.V.A - Just A Volunteer Away

A modern Swing-based GUI application connecting volunteers with elderly people who need help with daily tasks.

## 🎨 Modern UI Features

- **Professional Color Theme**: Orange and light blue color scheme
- **Styled Buttons**: Interactive buttons with hover effects
- **Custom Logo**: Display your own branding (replace `logo.png`)
- **Clean Design**: Modern, user-friendly interface

See [UI-SUMMARY.md](UI-SUMMARY.md) for visual improvements and [CUSTOMIZATION-GUIDE.md](CUSTOMIZATION-GUIDE.md) to customize the look.

## What It Does

- **Elderly users** create task requests (grocery shopping, doctor appointments, etc.)
- **Volunteers** browse and accept tasks to help
- **Points system** rewards volunteers with a leaderboard
- **Task management** allows reassignment if needed

## Requirements

- Java 8 or higher
- MySQL Server
- MySQL Connector JAR ([Download here](https://dev.mysql.com/downloads/connector/j/))

## Setup

### 1. Set up the database

```bash
```bash
# Log in to MySQL interactively
mysql -u root -p

# At the MySQL prompt, run:
SOURCE src/main/resources/sql/schema.sql;
```
```

This creates the database, tables, and sample users.

### 2. Download MySQL Connector

Download `mysql-connector-j-8.0.28.jar` and place it in the project root directory.

### 3. Update database credentials (if needed)

Edit `DatabaseManager.java` if your MySQL username/password is different:

```java
private static final String URL = "jdbc:mysql://localhost:3306/volunteer_app";
private static final String USER = "volunteer_user";  // Change if needed
private static final String PASSWORD = "volunteer_pass";  // Change if needed
```

## Running the Application

### Windows
```bash
compile.bat
run.bat
```

### Linux/Mac
```bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

## Test Accounts

| Username      | Password    | Role      |
| ------------- | ----------- | --------- |
| john_doe      | password123 | Elderly   |
| jane_smith    | password123 | Volunteer |
| bob_volunteer | password123 | Volunteer |
| admin         | admin123    | Admin     |

## Main Features

### For Elderly Users

- Create help requests
- View your tasks
- Remove volunteers and reassign tasks
- Confirm task completion (two-step verification)
- Delete tasks

### For Volunteers

- Browse available tasks
- Accept tasks
- Update task status
- Confirm task completion (two-step verification)
- View leaderboard

### For Administrators

- **View System Statistics** - Monitor platform health
- **View All Users** - Complete user overview with status
- **View All Tasks** - Comprehensive task listing
- **View Task History** - Complete audit trail of all activities
- **Manage Users** - Enable/disable user accounts
- **Manage Tasks** - Delete any task (admin override)

For detailed admin documentation, see [ADMIN-GUIDE.md](ADMIN-GUIDE.md)

### Two-Step Task Completion

Tasks require confirmation from **both volunteer and elderly** before being marked as COMPLETED:
- Volunteer marks task as complete → Status: PENDING_ELDERLY_CONFIRMATION
- Elderly confirms → Status: COMPLETED (points awarded!)
- Or vice versa (elderly confirms first)
- Once elderly confirms, volunteer cannot revert the status

For details, see [TWO-STEP-COMPLETION.md](TWO-STEP-COMPLETION.md)

### Points System

- Accept task: +10 points
- Complete task: +50 points
- Cancel task: -20 points

## Project Structure

```
├── User.java              # User model
├── Task.java              # Task model
├── DatabaseManager.java   # Database operations
├── VolunteerGUI.java      # GUI application (Swing)
├── compile.bat            # Compile script (Windows)
├── run.bat                # Run script (Windows)
├── compile.sh             # Compile script (Linux/Mac)
├── run.sh                 # Run script (Linux/Mac)
└── src/main/resources/sql/
    └── schema.sql         # Database setup
```

## Troubleshooting

**"ClassNotFoundException: com.mysql.cj.jdbc.Driver"**

- Make sure `mysql-connector-j-8.0.28.jar` is in the project root
- Check the path in `compile.bat` or `compile.sh`

**"Access denied for user"**

- Update credentials in `DatabaseManager.java`
- Or run: `mysql -u root -p < src/main/resources/sql/schema.sql` to create the default user

**"Unknown database 'volunteer_app'"**

- Run the schema.sql file first to create the database

---

Built with ❤️ to help communities connect and support each other.
