package com.java.volunteer.service.impl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.volunteer.dao.UserDao;
import com.java.volunteer.dao.impl.UserDaoImpl;
import com.java.volunteer.model.User;
import com.java.volunteer.service.UserService;

/**
 * Implementation of the UserService interface
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    
    // Email regex pattern for validation
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    // Password regex pattern for validation (at least 8 characters, one uppercase, one lowercase, one digit)
    private static final Pattern PASSWORD_PATTERN = 
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    
    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }
    
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User registerUser(String username, String password, String email, String firstName,
                            String lastName, String phoneNumber, String address,
                            LocalDate dateOfBirth, String role, String bio) throws SQLException, IllegalArgumentException {
        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Password must be at least 8 characters and include uppercase, lowercase, and a digit");
        }
        
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        
        // Validate role
        if (role == null || (!role.equals("ELDERLY") && !role.equals("VOLUNTEER") && !role.equals("ADMIN"))) {
            throw new IllegalArgumentException("Invalid role: must be ELDERLY, VOLUNTEER, or ADMIN");
        }
        
        // Check if username already exists
        User existingUser = userDao.findByUsername(username);
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Check if email already exists
        existingUser = userDao.findByEmail(email);
        if (existingUser != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        // Create and save the user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Password will be hashed in the DAO
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setDateOfBirth(dateOfBirth);
        user.setRole(role);
        user.setBio(bio);
        user.setActive(true);
        
        logger.info("Registering new {} user: {}", role, username);
        return userDao.save(user);
    }

    @Override
    public User authenticateUser(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        User user = userDao.findByUsername(username);
        
        if (user == null) {
            logger.warn("Authentication failed: username not found: {}", username);
            return null;
        }
        
        if (!user.isActive()) {
            logger.warn("Authentication failed: user is inactive: {}", username);
            return null;
        }
        
        // Check if the provided password matches the stored hashed password
        if (BCrypt.checkpw(password, user.getPassword())) {
            logger.info("User authenticated successfully: {}", username);
            return user;
        } else {
            logger.warn("Authentication failed: incorrect password for user: {}", username);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        return userDao.findAll();
    }

    @Override
    public List<User> getAllVolunteers() throws SQLException {
        return userDao.findByRole("VOLUNTEER");
    }

    @Override
    public List<User> getAllElderlyUsers() throws SQLException {
        return userDao.findByRole("ELDERLY");
    }

    @Override
    public User getUserById(int userId) throws SQLException {
        return userDao.findById(userId);
    }

    @Override
    public User updateUserProfile(User user) throws SQLException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        
        // Validate email if provided
        if (user.getEmail() != null && !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // Check if the user exists
        User existingUser = userDao.findById(user.getUserId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        // If the email is being changed, check if the new email is already in use
        if (!existingUser.getEmail().equals(user.getEmail())) {
            User userWithEmail = userDao.findByEmail(user.getEmail());
            if (userWithEmail != null && userWithEmail.getUserId() != user.getUserId()) {
                throw new IllegalArgumentException("Email already in use");
            }
        }
        
        // If the username is being changed, check if the new username is already in use
        if (!existingUser.getUsername().equals(user.getUsername())) {
            User userWithUsername = userDao.findByUsername(user.getUsername());
            if (userWithUsername != null && userWithUsername.getUserId() != user.getUserId()) {
                throw new IllegalArgumentException("Username already in use");
            }
        }
        
        logger.info("Updating user profile for ID: {}", user.getUserId());
        return userDao.update(user);
    }

    @Override
    public boolean changeUserActiveStatus(int userId, boolean active) throws SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        
        // Check if the user exists
        User existingUser = userDao.findById(userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        logger.info("Changing active status for user ID {}: {}", userId, active);
        return userDao.updateActiveStatus(userId, active);
    }
}