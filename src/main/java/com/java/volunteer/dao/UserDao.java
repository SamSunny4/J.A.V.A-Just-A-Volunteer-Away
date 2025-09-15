package com.java.volunteer.dao;

import java.sql.SQLException;
import java.util.List;

import com.java.volunteer.model.User;

/**
 * User DAO interface with operations specific to users
 */
public interface UserDao extends BaseDao<User, Integer> {
    
    /**
     * Find a user by username
     * 
     * @param username the username
     * @return the user or null if not found
     * @throws SQLException if a database error occurs
     */
    User findByUsername(String username) throws SQLException;
    
    /**
     * Find a user by email
     * 
     * @param email the email
     * @return the user or null if not found
     * @throws SQLException if a database error occurs
     */
    User findByEmail(String email) throws SQLException;
    
    /**
     * Find users by role
     * 
     * @param role the user role
     * @return a list of users with the specified role
     * @throws SQLException if a database error occurs
     */
    List<User> findByRole(String role) throws SQLException;
    
    /**
     * Change user active status
     * 
     * @param userId the user ID
     * @param active the new active status
     * @return true if the status was changed, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean updateActiveStatus(int userId, boolean active) throws SQLException;
}