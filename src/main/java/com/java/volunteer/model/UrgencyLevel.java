package com.java.volunteer.model;

/**
 * Enum representing task urgency levels in the system
 */
public enum UrgencyLevel {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String displayName;

    UrgencyLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}