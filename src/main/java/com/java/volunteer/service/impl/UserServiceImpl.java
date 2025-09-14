package com.java.volunteer.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.dao.UserDao;
import com.java.volunteer.model.User;
import com.java.volunteer.service.UserService;

/**
 * Implementation of UserService
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User register(User user) {
        // Check if username already exists
        if (userDao.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        // Check if email already exists
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        logger.info("Registering new user: {}", user.getUsername());
        return userDao.create(user);
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        logger.info("Authenticating user: {}", username);
        return userDao.authenticate(username, password);
    }

    @Override
    public Optional<User> getUserById(int userId) {
        return userDao.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public List<User> getUsersByRole(int roleId) {
        return userDao.findByRole(roleId);
    }

    @Override
    public User updateProfile(User user) {
        // Make sure the user exists
        Optional<User> existingUser = userDao.findById(user.getUserId());
        if (!existingUser.isPresent()) {
            throw new IllegalArgumentException("User not found: " + user.getUserId());
        }
        
        // Check if username already exists (if changed)
        if (!user.getUsername().equals(existingUser.get().getUsername()) &&
                userDao.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        
        // Check if email already exists (if changed)
        if (!user.getEmail().equals(existingUser.get().getEmail()) &&
                userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }
        
        // Preserve the password (don't update it here)
        user.setPassword(existingUser.get().getPassword());
        
        logger.info("Updating user profile: {}", user.getUsername());
        return userDao.update(user);
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // First authenticate with old password
        Optional<User> user = userDao.findById(userId);
        if (!user.isPresent()) {
            return false;
        }
        
        // Verify old password
        if (!userDao.authenticate(user.get().getUsername(), oldPassword).isPresent()) {
            return false;
        }
        
        logger.info("Changing password for user: {}", user.get().getUsername());
        return userDao.updatePassword(userId, newPassword);
    }

    @Override
    public boolean deactivateUser(int userId) {
        logger.info("Deactivating user ID: {}", userId);
        return userDao.updateActiveStatus(userId, false);
    }

    @Override
    public boolean activateUser(int userId) {
        logger.info("Activating user ID: {}", userId);
        return userDao.updateActiveStatus(userId, true);
    }
}