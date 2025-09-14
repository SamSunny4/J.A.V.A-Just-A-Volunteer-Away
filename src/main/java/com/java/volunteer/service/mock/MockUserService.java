package com.java.volunteer.service.mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.java.volunteer.model.User;
import com.java.volunteer.model.UserRole;
import com.java.volunteer.service.UserService;

/**
 * Mock implementation of UserService for testing and development
 */
public class MockUserService implements UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;
    
    public MockUserService() {
        // Create some sample users
        createSampleUsers();
    }
    
    private void createSampleUsers() {
        // Create an admin
        User admin = new User();
        admin.setUserId(nextId++);
        admin.setUsername("admin");
        admin.setPassword("$2a$10$XOPEUldsjJlDaD9UOZ1.sOIlwTPiaq5KRXsnoHr9bKIvTnMaBVLpW"); // "admin"
        admin.setFirstName("System");
        admin.setLastName("Administrator");
        admin.setEmail("admin@example.com");
        admin.setPhoneNumber("555-0000");
        admin.setRoleId(UserRole.ROLE_ADMIN);
        admin.setActive(true);
        admin.setCreatedAt(LocalDateTime.now());
        users.put(admin.getUserId(), admin);
        
        // Create an elderly user
        User elderly = new User();
        elderly.setUserId(nextId++);
        elderly.setUsername("elderly1");
        elderly.setPassword("$2a$10$XZ2YEW8OolNQvP0JdNYYUOIRS7SxwjV3xXdEfRmiGIVvP3daL0VQS"); // "password"
        elderly.setFirstName("John");
        elderly.setLastName("Smith");
        elderly.setEmail("john@example.com");
        elderly.setPhoneNumber("555-1234");
        elderly.setAddress("123 Main St, Anytown");
        elderly.setDateOfBirth(LocalDate.of(1950, 5, 15));
        elderly.setRoleId(UserRole.ROLE_ELDERLY);
        elderly.setActive(true);
        elderly.setCreatedAt(LocalDateTime.now());
        users.put(elderly.getUserId(), elderly);
        
        // Create a volunteer
        User volunteer = new User();
        volunteer.setUserId(nextId++);
        volunteer.setUsername("volunteer1");
        volunteer.setPassword("$2a$10$XZ2YEW8OolNQvP0JdNYYUOIRS7SxwjV3xXdEfRmiGIVvP3daL0VQS"); // "password"
        volunteer.setFirstName("Jane");
        volunteer.setLastName("Doe");
        volunteer.setEmail("jane@example.com");
        volunteer.setPhoneNumber("555-5678");
        volunteer.setAddress("456 Oak St, Anytown");
        volunteer.setDateOfBirth(LocalDate.of(1995, 8, 22));
        volunteer.setRoleId(UserRole.ROLE_VOLUNTEER);
        volunteer.setActive(true);
        volunteer.setCreatedAt(LocalDateTime.now());
        users.put(volunteer.getUserId(), volunteer);
    }

    @Override
    public User register(User user) {
        user.setUserId(nextId++);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        // In a real implementation, we would hash the password
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        // For testing purposes, we'll accept any password
        return users.values().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> getUsersByRole(int roleId) {
        return users.values().stream()
            .filter(user -> user.getRoleId() == roleId)
            .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public User updateProfile(User user) {
        if (users.containsKey(user.getUserId())) {
            user.setUpdatedAt(LocalDateTime.now());
            users.put(user.getUserId(), user);
            return user;
        }
        return null;
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if (users.containsKey(userId)) {
            User user = users.get(userId);
            user.setPassword(newPassword); // In real impl, would hash
            user.setUpdatedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivateUser(int userId) {
        if (users.containsKey(userId)) {
            User user = users.get(userId);
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }

    @Override
    public boolean activateUser(int userId) {
        if (users.containsKey(userId)) {
            User user = users.get(userId);
            user.setActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }
}