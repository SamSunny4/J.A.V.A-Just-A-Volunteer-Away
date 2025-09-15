package com.java.volunteer.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Task model representing volunteer tasks requested by elderly users
 */
public class Task {
    private int taskId;
    private String title;
    private String description;
    private int requesterId; // Elderly user who requested the task
    private Integer volunteerId; // Volunteer assigned to the task (may be null if not assigned)
    private String status; // PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED, AVAILABLE, REASSIGNED
    private String location;
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
    private int estimatedDuration; // in minutes
    private String urgencyLevel; // LOW, MEDIUM, HIGH
    private Integer previousVolunteerId; // Previous volunteer if task was reassigned
    private String reassignmentReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Task() {
    }

    // Constructor with required fields for creating a new task
    public Task(String title, String description, int requesterId, String location,
                LocalDate scheduledDate, LocalTime scheduledTime, int estimatedDuration, String urgencyLevel) {
        this.title = title;
        this.description = description;
        this.requesterId = requesterId;
        this.location = location;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.estimatedDuration = estimatedDuration;
        this.urgencyLevel = urgencyLevel;
        this.status = "AVAILABLE";
    }

    // Full constructor
    public Task(int taskId, String title, String description, int requesterId, Integer volunteerId,
                String status, String location, LocalDate scheduledDate, LocalTime scheduledTime,
                int estimatedDuration, String urgencyLevel, Integer previousVolunteerId,
                String reassignmentReason, LocalDateTime createdAt, LocalDateTime updatedAt) {
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
        this.urgencyLevel = urgencyLevel;
        this.previousVolunteerId = previousVolunteerId;
        this.reassignmentReason = reassignmentReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
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

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", requesterId=" + requesterId +
                ", volunteerId=" + volunteerId +
                ", status='" + status + '\'' +
                ", location='" + location + '\'' +
                ", scheduledDate=" + scheduledDate +
                ", scheduledTime=" + scheduledTime +
                ", estimatedDuration=" + estimatedDuration +
                ", urgencyLevel='" + urgencyLevel + '\'' +
                '}';
    }
}