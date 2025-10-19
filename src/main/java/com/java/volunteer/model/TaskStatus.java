package com.java.volunteer.model;

/**
 * Enum representing task statuses in the system
 */
public enum TaskStatus {
    PENDING("Pending"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    AVAILABLE("Available"),
    REASSIGNED("Reassigned");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}