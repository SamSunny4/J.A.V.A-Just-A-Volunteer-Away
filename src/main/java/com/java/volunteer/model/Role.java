package com.java.volunteer.model;

/**
 * Enum representing user roles in the system
 */
public enum Role {
    ADMIN("Admin"),
    ELDERLY("Elderly"),
    VOLUNTEER("Volunteer");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}