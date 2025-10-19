/**
 * Task class representing tasks created by elderly users
 */
public class Task {
    private int taskId;
    private String title;
    private String description;
    private int requesterId; // Elderly user who created the task
    private Integer volunteerId; // Volunteer assigned (null if not assigned)
    private String status; // "AVAILABLE", "ASSIGNED", "IN_PROGRESS", "COMPLETED", "CANCELLED"
    private String location;
    private String scheduledDate; // Format: YYYY-MM-DD
    private String scheduledTime; // Format: HH:MM 
    private int estimatedDuration; // in minutes
    
    // Constructor for new tasks
    public Task(String title, String description, int requesterId, String location, 
                String scheduledDate, String scheduledTime, int estimatedDuration) {
        this.title = title;
        this.description = description;
        this.requesterId = requesterId;
        this.location = location;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.estimatedDuration = estimatedDuration;
        this.status = "AVAILABLE";
        this.volunteerId = null;
    }
    
    // Constructor for existing tasks (from database)
    public Task(int taskId, String title, String description, int requesterId, 
                Integer volunteerId, String status, String location, 
                String scheduledDate, String scheduledTime, int estimatedDuration) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.requesterId = requesterId;
        this.volunteerId = volunteerId;
        this.status = status;
        this.location = location;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.estimatedDuration = estimatedDuration;
    }
    
    // Getters
    public int getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getRequesterId() { return requesterId; }
    public Integer getVolunteerId() { return volunteerId; }
    public String getStatus() { return status; }
    public String getLocation() { return location; }
    public String getScheduledDate() { return scheduledDate; }
    public String getScheduledTime() { return scheduledTime; }
    public int getEstimatedDuration() { return estimatedDuration; }
    
    // Setters
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public void setVolunteerId(Integer volunteerId) { this.volunteerId = volunteerId; }
    public void setStatus(String status) { this.status = status; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return String.format("Task[%d]: %s - %s at %s %s (Duration: %d min) [%s]",
            taskId, title, location, scheduledDate, scheduledTime, estimatedDuration, status);
    }
}
