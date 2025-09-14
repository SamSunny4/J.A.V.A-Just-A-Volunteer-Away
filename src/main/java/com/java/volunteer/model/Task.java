package com.java.volunteer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Task entity class that represents a task in the system
 */
public class Task {
    // Enum for task status
    public enum Status {
        PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED, AVAILABLE, REASSIGNED
    }
    
    // Enum for task urgency level
    public enum UrgencyLevel {
        LOW, MEDIUM, HIGH
    }

    private int taskId;
    private String title;
    private String description;
    private int requesterId;
    private Integer volunteerId;
    private Status status;
    private String location;
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
    private int estimatedDuration; // in minutes
    private UrgencyLevel urgencyLevel;
    private Integer previousVolunteerId;
    private String reassignmentReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // References for convenience (not stored in DB)
    private User requester;
    private User volunteer;
    private User previousVolunteer;

    // Default constructor
    public Task() {
        this.status = Status.AVAILABLE;
        this.urgencyLevel = UrgencyLevel.MEDIUM;
    }

    // Constructor with essential fields
    public Task(String title, String description, int requesterId, LocalDate scheduledDate, 
                LocalTime scheduledTime, int estimatedDuration, UrgencyLevel urgencyLevel) {
        this.title = title;
        this.description = description;
        this.requesterId = requesterId;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.estimatedDuration = estimatedDuration;
        this.urgencyLevel = urgencyLevel;
        this.status = Status.AVAILABLE;
    }

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public Integer getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Integer volunteerId) {
        this.volunteerId = volunteerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public UrgencyLevel getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(UrgencyLevel urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public Integer getPreviousVolunteerId() {
        return previousVolunteerId;
    }

    public void setPreviousVolunteerId(Integer previousVolunteerId) {
        this.previousVolunteerId = previousVolunteerId;
    }

    public String getReassignmentReason() {
        return reassignmentReason;
    }

    public void setReassignmentReason(String reassignmentReason) {
        this.reassignmentReason = reassignmentReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(User volunteer) {
        this.volunteer = volunteer;
    }

    public User getPreviousVolunteer() {
        return previousVolunteer;
    }

    public void setPreviousVolunteer(User previousVolunteer) {
        this.previousVolunteer = previousVolunteer;
    }
    
    // Method to assign a volunteer to this task
    public void assignVolunteer(int volunteerId) {
        this.volunteerId = volunteerId;
        this.status = Status.ASSIGNED;
    }
    
    // Method to start a task
    public void startTask() {
        if (this.status == Status.ASSIGNED) {
            this.status = Status.IN_PROGRESS;
        }
    }
    
    // Method to complete a task
    public void completeTask() {
        if (this.status == Status.IN_PROGRESS || this.status == Status.ASSIGNED) {
            this.status = Status.COMPLETED;
        }
    }
    
    // Method to cancel a task
    public void cancelTask() {
        this.status = Status.CANCELLED;
    }
    
    // Method to reassign a task from current volunteer
    public void reassignTask(String reason) {
        if (this.volunteerId != null) {
            this.previousVolunteerId = this.volunteerId;
            this.volunteerId = null;
            this.reassignmentReason = reason;
            this.status = Status.AVAILABLE;
        }
    }
    
    @Override
    public String toString() {
        String requesterName = (requester != null) ? requester.getUsername() : "Unknown";
        String volunteerName = (volunteer != null) ? volunteer.getUsername() : "Unassigned";
        
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", scheduledDate=" + scheduledDate +
                ", scheduledTime=" + scheduledTime +
                ", estimatedDuration=" + estimatedDuration + " minutes" +
                ", urgencyLevel=" + urgencyLevel +
                ", requester=" + requesterName +
                ", volunteer=" + volunteerName +
                '}';
    }
}