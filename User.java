
public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber; 
    private String role; // "ELDERLY", "VOLUNTEER", or "ADMIN"
    private int points;
    private int tasksCompleted;
    private boolean isActive; // For account status
    
    // Constructor for new users (registration)
    public User(String username, String password, String email, String firstName, 
                String lastName, String phoneNumber, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.points = 0;
        this.tasksCompleted = 0;
        this.isActive = true; // Default to active
    }
    
    // Constructor for existing users (from database)
    public User(int userId, String username, String email, String firstName, 
                String lastName, String phoneNumber, String role, int points, int tasksCompleted) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.points = points;
        this.tasksCompleted = tasksCompleted;
        this.isActive = true; // Default to active
    }
    
    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }
    public int getPoints() { return points; }
    public int getTasksCompleted() { return tasksCompleted; }
    public boolean isActive() { return isActive; }
    
    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPoints(int points) { this.points = points; }
    public void setTasksCompleted(int tasksCompleted) { this.tasksCompleted = tasksCompleted; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    
    @Override
    public String toString() {
        return String.format("User[%d]: %s %s (%s) - Role: %s, Points: %d, Tasks: %d",
            userId, firstName, lastName, username, role, points, tasksCompleted);
    }
}
