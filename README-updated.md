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

- Java 11 or higher
- MySQL Database
- Maven

### Installation

1. **Clone the repository:**

   ```sh
   git clone https://github.com/your-username/J.A.V.A-Just-A-Volunteer-Away.git
   ```

2. **Navigate to the project folder:**

   ```sh
   cd J.A.V.A-Just-A-Volunteer-Away
   ```

3. **Set up the MySQL database:**

   - Create a new MySQL database (e.g., `volunteer_app`).
   - Import the SQL schema from `src/main/resources/sql/schema.sql`.

4. **Configure database connection:**

   - Edit `src/main/resources/db.properties` with your MySQL credentials.

5. **Build the application:**

   ```sh
   mvn clean package
   ```

6. **Run the application:**

   ```sh
   java -jar target/volunteer-app-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

## Demo Accounts

The application comes with demo accounts for testing:

- **Admin**: Username: `admin`, Password: `admin`
- **Elderly**: Username: `elderly1`, Password: `password`
- **Volunteer**: Username: `volunteer1`, Password: `password`

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