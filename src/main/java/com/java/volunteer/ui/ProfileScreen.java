package com.java.volunteer.ui;

import java.util.Scanner;

import com.java.volunteer.model.User;
import com.java.volunteer.model.UserRole;
import com.java.volunteer.service.UserService;

/**
 * Screen for viewing and editing user profile information
 */
public class ProfileScreen extends BaseScreen {
    private final UserService userService;
    
    public ProfileScreen(User currentUser, UserService userService) {
        super(currentUser);
        this.userService = userService;
    }
    
    @Override
    public BaseScreen display() {
        showHeader("My Profile");
        
        System.out.println("\nProfile Information:");
        System.out.println("--------------------");
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Name: " + currentUser.getFullName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Role: " + getUserRoleName(currentUser.getRoleId()));
        System.out.println("Address: " + (currentUser.getAddress() != null ? currentUser.getAddress() : "Not provided"));
        System.out.println("Phone: " + (currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "Not provided"));
        
        int choice = showMenu(
            "Edit Profile",
            "Change Password",
            "Back to Main Menu"
        );
        
        switch (choice) {
            case 1:
                return editProfile();
            case 2:
                return changePassword();
            case 3:
                return new MainMenuScreen(currentUser, userService, null, null);
            case 0:
                return null;
            default:
                return this;
        }
    }
    
    private String getUserRoleName(int roleId) {
        if (roleId == UserRole.ROLE_ELDERLY) {
            return "Elderly";
        } else if (roleId == UserRole.ROLE_VOLUNTEER) {
            return "Volunteer";
        } else if (roleId == UserRole.ROLE_ADMIN) {
            return "Administrator";
        } else {
            return "Unknown Role";
        }
    }
    
    private BaseScreen editProfile() {
        showHeader("Edit Profile");
        
        Scanner scanner = new Scanner(System.in);
        String firstName, lastName, address, phone;
        
        System.out.print("New first name (leave blank to keep current): ");
        firstName = scanner.nextLine();
        
        System.out.print("New last name (leave blank to keep current): ");
        lastName = scanner.nextLine();
        
        System.out.print("New address (leave blank to keep current): ");
        address = scanner.nextLine();
        
        System.out.print("New phone number (leave blank to keep current): ");
        phone = scanner.nextLine();
        
        if (!firstName.isEmpty()) {
            currentUser.setFirstName(firstName);
        }
        
        if (!lastName.isEmpty()) {
            currentUser.setLastName(lastName);
        }
        
        if (!address.isEmpty()) {
            currentUser.setAddress(address);
        }
        
        if (!phone.isEmpty()) {
            currentUser.setPhoneNumber(phone);
        }
        
        User updatedUser = userService.updateProfile(currentUser);
        
        if (updatedUser != null) {
            showMessage("Profile updated successfully!");
            currentUser = updatedUser; // Update with the returned user
        } else {
            showMessage("Failed to update profile.");
        }
        
        return this;
    }
    
    private BaseScreen changePassword() {
        showHeader("Change Password");
        
        String currentPassword = getRequiredInput("Current password");
        String newPassword = getRequiredInput("New password");
        String confirmPassword = getRequiredInput("Confirm new password");
        
        if (!newPassword.equals(confirmPassword)) {
            showMessage("New passwords do not match.");
            return this;
        }
        
        boolean success = userService.changePassword(currentUser.getUserId(), currentPassword, newPassword);
        
        if (success) {
            showMessage("Password changed successfully!");
        } else {
            showMessage("Failed to change password. Make sure your current password is correct.");
        }
        
        return this;
    }
}