import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager handles all database operations using JDBC
 * Connects to MySQL database and provides methods for user and task management
 */
public class DatabaseManager {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/volunteer_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    // Load MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }
    
    // Get database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    // ==================== USER OPERATIONS ====================
    
    /**
     * Register a new user (elderly or volunteer)
     */
    public static boolean registerUser(User user) {
        String sql = "INSERT INTO users (username, password, email, first_name, last_name, " +
                     "phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getPhoneNumber());
            stmt.setString(7, user.getRole()); // "ELDERLY" or "VOLUNTEER"
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated user ID
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                    
                    // Create user_points entry for volunteers
                    if (user.getRole().equals("VOLUNTEER")) {
                        createUserPoints(user.getUserId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Authenticate user login
     */
    public static User loginUser(String username, String password) {
        String sql = "SELECT u.user_id, u.username, u.email, u.first_name, u.last_name, " +
                     "u.phone_number, u.role, COALESCE(up.points, 0) as points, " +
                     "COALESCE(up.tasks_completed, 0) as tasks_completed " +
                     "FROM users u " +
                     "LEFT JOIN user_points up ON u.user_id = up.user_id " +
                     "WHERE u.username = ? AND u.password = ? AND u.is_active = TRUE";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone_number"),
                    rs.getString("role"),
                    rs.getInt("points"),
                    rs.getInt("tasks_completed")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error logging in: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create user_points entry for a new volunteer
     */
    private static void createUserPoints(int userId) {
        String sql = "INSERT INTO user_points (user_id, points, level, user_rank, tasks_completed) " +
                     "VALUES (?, 0, 1, 'Newcomer', 0)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating user points: " + e.getMessage());
        }
    }
    
    /**
     * Get leaderboard (top volunteers by points)
     */
    public static List<User> getLeaderboard(int limit) {
        List<User> leaderboard = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.email, u.first_name, u.last_name, " +
                     "u.phone_number, u.role, up.points, up.tasks_completed " +
                     "FROM users u " +
                     "JOIN user_points up ON u.user_id = up.user_id " +
                     "WHERE u.role = 'VOLUNTEER' " +
                     "ORDER BY up.points DESC, up.tasks_completed DESC " +
                     "LIMIT ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                leaderboard.add(new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone_number"),
                    rs.getString("role"),
                    rs.getInt("points"),
                    rs.getInt("tasks_completed")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting leaderboard: " + e.getMessage());
        }
        return leaderboard;
    }
    
    // ==================== TASK OPERATIONS ====================
    
    /**
     * Create a new task
     */
    public static boolean createTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, requester_id, status, location, " +
                     "scheduled_date, scheduled_time, estimated_duration) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setInt(3, task.getRequesterId());
            stmt.setString(4, task.getStatus());
            stmt.setString(5, task.getLocation());
            stmt.setString(6, task.getScheduledDate());
            stmt.setString(7, task.getScheduledTime());
            stmt.setInt(8, task.getEstimatedDuration());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    task.setTaskId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all available tasks
     */
    public static List<Task> getAvailableTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE status = 'AVAILABLE' ORDER BY scheduled_date, scheduled_time";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tasks.add(createTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available tasks: " + e.getMessage());
        }
        return tasks;
    }
    
    /**
     * Get tasks by requester (elderly user)
     */
    public static List<Task> getTasksByRequester(int requesterId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE requester_id = ? ORDER BY scheduled_date DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requesterId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(createTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting tasks by requester: " + e.getMessage());
        }
        return tasks;
    }
    
    /**
     * Get tasks assigned to a volunteer
     */
    public static List<Task> getTasksByVolunteer(int volunteerId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE volunteer_id = ? ORDER BY scheduled_date";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(createTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting tasks by volunteer: " + e.getMessage());
        }
        return tasks;
    }
    
    /**
     * Assign a task to a volunteer
     */
    public static boolean assignTask(int taskId, int volunteerId) {
        String sql = "UPDATE tasks SET volunteer_id = ?, status = 'ASSIGNED' " +
                     "WHERE task_id = ? AND status = 'AVAILABLE'";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, volunteerId);
            stmt.setInt(2, taskId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning task: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update task status
     */
    public static boolean updateTaskStatus(int taskId, String status) {
        String sql = "UPDATE tasks SET status = ? WHERE task_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, taskId);
            
            int rowsAffected = stmt.executeUpdate();
            
            // If task completed, update volunteer points
            if (rowsAffected > 0 && status.equals("COMPLETED")) {
                Task task = getTaskById(taskId);
                if (task != null && task.getVolunteerId() != null) {
                    updateVolunteerPoints(task.getVolunteerId(), task.getEstimatedDuration());
                }
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating task status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Volunteer marks task as completed (requires elderly confirmation)
     */
    public static boolean volunteerConfirmTask(int taskId, int volunteerId) {
        // Check current state
        Task task = getTaskById(taskId);
        if (task == null || task.getVolunteerId() == null || task.getVolunteerId() != volunteerId) {
            return false;
        }
        
        // If elderly already confirmed, mark as COMPLETED
        if (task.isElderlyConfirmed()) {
            String sql = "UPDATE tasks SET volunteer_confirmed = TRUE, status = 'COMPLETED' WHERE task_id = ? AND volunteer_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, taskId);
                stmt.setInt(2, volunteerId);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    updateVolunteerPoints(volunteerId, task.getEstimatedDuration());
                    return true;
                }
            } catch (SQLException e) {
                System.err.println("Error confirming task: " + e.getMessage());
            }
        } else {
            // Elderly hasn't confirmed yet, set to PENDING_ELDERLY_CONFIRMATION
            String sql = "UPDATE tasks SET volunteer_confirmed = TRUE, status = 'PENDING_ELDERLY_CONFIRMATION' WHERE task_id = ? AND volunteer_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, taskId);
                stmt.setInt(2, volunteerId);
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Error confirming task: " + e.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Elderly confirms task completion (requires volunteer confirmation)
     */
    public static boolean elderlyConfirmTask(int taskId, int requesterId) {
        // Check current state
        Task task = getTaskById(taskId);
        if (task == null || task.getRequesterId() != requesterId) {
            return false;
        }
        
        // Cannot revert if volunteer already confirmed
        if (task.isVolunteerConfirmed() && task.isElderlyConfirmed()) {
            return false; // Already fully completed
        }
        
        // If volunteer already confirmed, mark as COMPLETED
        if (task.isVolunteerConfirmed()) {
            String sql = "UPDATE tasks SET elderly_confirmed = TRUE, status = 'COMPLETED' WHERE task_id = ? AND requester_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, taskId);
                stmt.setInt(2, requesterId);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0 && task.getVolunteerId() != null) {
                    updateVolunteerPoints(task.getVolunteerId(), task.getEstimatedDuration());
                    return true;
                }
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.err.println("Error confirming task: " + e.getMessage());
            }
        } else {
            // Volunteer hasn't confirmed yet, set to PENDING_VOLUNTEER_CONFIRMATION
            String sql = "UPDATE tasks SET elderly_confirmed = TRUE, status = 'PENDING_VOLUNTEER_CONFIRMATION' WHERE task_id = ? AND requester_id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, taskId);
                stmt.setInt(2, requesterId);
                
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Error confirming task: " + e.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Reassign task (remove volunteer, make available again)
     */
    public static boolean reassignTask(int taskId) {
        String sql = "UPDATE tasks SET volunteer_id = NULL, status = 'AVAILABLE', " +
                     "volunteer_confirmed = FALSE, elderly_confirmed = FALSE, " +
                     "previous_volunteer_id = volunteer_id, " +
                     "reassignment_reason = 'Removed by requester' " +
                     "WHERE task_id = ? AND volunteer_id IS NOT NULL";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error reassigning task: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a task (only if it hasn't been assigned or completed)
     */
    public static boolean deleteTask(int taskId, int requesterId) {
        String sql = "DELETE FROM tasks WHERE task_id = ? AND requester_id = ? AND " +
                     "(status = 'AVAILABLE' OR status = 'CANCELLED')";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, requesterId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Cancel a task (marks as cancelled instead of deleting)
     */
    public static boolean cancelTask(int taskId, int requesterId) {
        String sql = "UPDATE tasks SET status = 'CANCELLED' WHERE task_id = ? AND requester_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, requesterId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelling task: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get a task by ID
     */
    private static Task getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createTaskFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting task by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update volunteer points after completing a task
     */
    private static void updateVolunteerPoints(int volunteerId, int durationMinutes) {
        // Award points: 10 points per 30 minutes of work
        int pointsToAdd = (durationMinutes / 30) * 10;
        
        String sql = "UPDATE user_points SET points = points + ?, tasks_completed = tasks_completed + 1, " +
                     "hours_volunteered = hours_volunteered + ? WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pointsToAdd);
            stmt.setInt(2, durationMinutes / 60);
            stmt.setInt(3, volunteerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating volunteer points: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to create Task object from ResultSet
     */
    private static Task createTaskFromResultSet(ResultSet rs) throws SQLException {
        Integer volunteerId = rs.getInt("volunteer_id");
        if (rs.wasNull()) {
            volunteerId = null;
        }
        
        return new Task(
            rs.getInt("task_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getInt("requester_id"),
            volunteerId,
            rs.getString("status"),
            rs.getString("location"),
            rs.getString("scheduled_date"),
            rs.getString("scheduled_time"), 
            rs.getInt("estimated_duration"),
            rs.getBoolean("volunteer_confirmed"),
            rs.getBoolean("elderly_confirmed")
        );
    }
    
    /**
     * Test database connection
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== ADMIN OPERATIONS ====================
    
    /**
     * Get all users (for admin panel)
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.email, u.first_name, u.last_name, " +
                     "u.phone_number, u.role, u.is_active, COALESCE(up.points, 0) as points, " +
                     "COALESCE(up.tasks_completed, 0) as tasks_completed " +
                     "FROM users u " +
                     "LEFT JOIN user_points up ON u.user_id = up.user_id " +
                     "ORDER BY u.user_id";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("phone_number"),
                    rs.getString("role"),
                    rs.getInt("points"),
                    rs.getInt("tasks_completed")
                );
                user.setActive(rs.getBoolean("is_active"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Get all tasks (for admin panel)
     */
    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY task_id DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                tasks.add(createTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all tasks: " + e.getMessage());
        }
        return tasks;
    }
    
    /**
     * Disable/Enable user account (admin function)
     */
    public static boolean toggleUserStatus(int userId, boolean isActive) {
        String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, isActive);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            
            // If disabling user, also cancel their active tasks
            if (rowsAffected > 0 && !isActive) {
                cancelUserActiveTasks(userId);
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error toggling user status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Cancel all active tasks for a user (when account is disabled)
     */
    private static void cancelUserActiveTasks(int userId) {
        // Cancel tasks as requester
        String sql1 = "UPDATE tasks SET status = 'CANCELLED' " +
                      "WHERE requester_id = ? AND status NOT IN ('COMPLETED', 'CANCELLED')";
        
        // Remove volunteer from assigned tasks
        String sql2 = "UPDATE tasks SET volunteer_id = NULL, status = 'AVAILABLE', " +
                      "volunteer_confirmed = FALSE, elderly_confirmed = FALSE " +
                      "WHERE volunteer_id = ? AND status NOT IN ('COMPLETED', 'CANCELLED')";
        
        try (Connection conn = getConnection()) {
            try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
                stmt1.setInt(1, userId);
                stmt1.executeUpdate();
            }
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setInt(1, userId);
                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error cancelling user tasks: " + e.getMessage());
        }
    }
    
    /**
     * Delete task permanently (admin function)
     */
    public static boolean adminDeleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get task history/activity log
     */
    public static List<String> getTaskHistory() {
        List<String> history = new ArrayList<>();
        String sql = "SELECT th.history_id, th.action_type, th.previous_status, th.new_status, " +
                     "th.changed_at, t.title as task_title, t.task_id, " +
                     "u.username as changed_by, u.role as user_role " +
                     "FROM task_history th " +
                     "JOIN tasks t ON th.task_id = t.task_id " +
                     "JOIN users u ON th.changed_by_id = u.user_id " +
                     "ORDER BY th.changed_at DESC LIMIT 100";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String entry = String.format("[%s] %s (%s) - Task #%d '%s': %s %s -> %s",
                    rs.getString("changed_at"),
                    rs.getString("changed_by"),
                    rs.getString("user_role"),
                    rs.getInt("task_id"),
                    rs.getString("task_title"),
                    rs.getString("action_type"),
                    rs.getString("previous_status") != null ? rs.getString("previous_status") : "N/A",
                    rs.getString("new_status") != null ? rs.getString("new_status") : "N/A"
                );
                history.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("Error getting task history: " + e.getMessage());
        }
        return history;
    }
    
    /**
     * Add task history entry
     */
    public static void addTaskHistory(int taskId, int userId, String actionType, 
                                      String previousStatus, String newStatus) {
        String sql = "INSERT INTO task_history (task_id, changed_by_id, action_type, " +
                     "previous_status, new_status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            stmt.setString(3, actionType);
            stmt.setString(4, previousStatus);
            stmt.setString(5, newStatus);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding task history: " + e.getMessage());
        }
    }
    
    /**
     * Get system statistics (for admin dashboard)
     */
    public static String getSystemStats() {
        StringBuilder stats = new StringBuilder();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Total users
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users WHERE is_active = TRUE");
            if (rs.next()) {
                stats.append("Active Users: ").append(rs.getInt("count")).append("\n");
            }
            
            // Total volunteers
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users WHERE role = 'VOLUNTEER' AND is_active = TRUE");
            if (rs.next()) {
                stats.append("Active Volunteers: ").append(rs.getInt("count")).append("\n");
            }
            
            // Total elderly
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users WHERE role = 'ELDERLY' AND is_active = TRUE");
            if (rs.next()) {
                stats.append("Active Elderly: ").append(rs.getInt("count")).append("\n");
            }
            
            // Total tasks
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM tasks");
            if (rs.next()) {
                stats.append("Total Tasks: ").append(rs.getInt("count")).append("\n");
            }
            
            // Available tasks
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM tasks WHERE status = 'AVAILABLE'");
            if (rs.next()) {
                stats.append("Available Tasks: ").append(rs.getInt("count")).append("\n");
            }
            
            // Completed tasks
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM tasks WHERE status = 'COMPLETED'");
            if (rs.next()) {
                stats.append("Completed Tasks: ").append(rs.getInt("count")).append("\n");
            }
            
            // In progress tasks
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM tasks WHERE status IN ('ASSIGNED', 'IN_PROGRESS', 'PENDING_ELDERLY_CONFIRMATION', 'PENDING_VOLUNTEER_CONFIRMATION')");
            if (rs.next()) {
                stats.append("In Progress Tasks: ").append(rs.getInt("count")).append("\n");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting system stats: " + e.getMessage());
        }
        
        return stats.toString();
    }
}
