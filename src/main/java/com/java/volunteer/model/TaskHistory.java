package com.java.volunteer.model;

import java.time.LocalDateTime;

/**
 * Entity class for tracking task history
 */
public class TaskHistory {
    public enum ChangedBy {
        VOLUNTEER, ELDERLY, ADMIN
    }

    private int historyId;
    private int taskId;
    private int changedById;
    private ChangedBy changedBy;
    private Task.Status previousStatus;
    private Task.Status newStatus;
    private Integer previousVolunteerId;
    private Integer newVolunteerId;
    private String reassignmentReason;
    private String notes;
    private LocalDateTime changedAt;
    
    // References for convenience
    private Task task;
    private User changedByUser;
    private User previousVolunteer;
    private User newVolunteer;

    public TaskHistory() {
    }

    // Constructor for status changes
    public TaskHistory(int taskId, int changedById, ChangedBy changedBy, 
                       Task.Status previousStatus, Task.Status newStatus, String notes) {
        this.taskId = taskId;
        this.changedById = changedById;
        this.changedBy = changedBy;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.notes = notes;
    }
    
    // Constructor for volunteer reassignment
    public TaskHistory(int taskId, int changedById, ChangedBy changedBy, 
                     Task.Status previousStatus, Task.Status newStatus, 
                     Integer previousVolunteerId, Integer newVolunteerId, 
                     String reassignmentReason, String notes) {
        this.taskId = taskId;
        this.changedById = changedById;
        this.changedBy = changedBy;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.previousVolunteerId = previousVolunteerId;
        this.newVolunteerId = newVolunteerId;
        this.reassignmentReason = reassignmentReason;
        this.notes = notes;
    }

    // Getters and Setters
    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getChangedById() {
        return changedById;
    }

    public void setChangedById(int changedById) {
        this.changedById = changedById;
    }

    public ChangedBy getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(ChangedBy changedBy) {
        this.changedBy = changedBy;
    }

    public Task.Status getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(Task.Status previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Task.Status getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Task.Status newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getPreviousVolunteerId() {
        return previousVolunteerId;
    }

    public void setPreviousVolunteerId(Integer previousVolunteerId) {
        this.previousVolunteerId = previousVolunteerId;
    }

    public Integer getNewVolunteerId() {
        return newVolunteerId;
    }

    public void setNewVolunteerId(Integer newVolunteerId) {
        this.newVolunteerId = newVolunteerId;
    }

    public String getReassignmentReason() {
        return reassignmentReason;
    }

    public void setReassignmentReason(String reassignmentReason) {
        this.reassignmentReason = reassignmentReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getChangedByUser() {
        return changedByUser;
    }

    public void setChangedByUser(User changedByUser) {
        this.changedByUser = changedByUser;
    }

    public User getPreviousVolunteer() {
        return previousVolunteer;
    }

    public void setPreviousVolunteer(User previousVolunteer) {
        this.previousVolunteer = previousVolunteer;
    }

    public User getNewVolunteer() {
        return newVolunteer;
    }

    public void setNewVolunteer(User newVolunteer) {
        this.newVolunteer = newVolunteer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("History [").append(historyId).append("]: ");
        sb.append("Task #").append(taskId).append(" - ");
        sb.append("Changed by ").append(changedBy).append(": ");
        sb.append(previousStatus).append(" -> ").append(newStatus);
        
        if (previousVolunteerId != null || newVolunteerId != null) {
            sb.append(" | Volunteer reassignment: ");
            sb.append(previousVolunteerId != null ? previousVolunteerId : "None");
            sb.append(" -> ");
            sb.append(newVolunteerId != null ? newVolunteerId : "None");
            if (reassignmentReason != null && !reassignmentReason.isEmpty()) {
                sb.append(" (Reason: ").append(reassignmentReason).append(")");
            }
        }
        
        if (notes != null && !notes.isEmpty()) {
            sb.append(" | Notes: ").append(notes);
        }
        
        sb.append(" | ").append(changedAt);
        return sb.toString();
    }
}