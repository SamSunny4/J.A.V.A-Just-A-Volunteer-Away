package com.java.volunteer.model;

/**
 * Entity class for user roles
 */
public class UserRole {
    private int roleId;
    private String roleName;

    public UserRole() {
    }

    public UserRole(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
    
    // Constants for common roles
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_ELDERLY = 2;
    public static final int ROLE_VOLUNTEER = 3;
}