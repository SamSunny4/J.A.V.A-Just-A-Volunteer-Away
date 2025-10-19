package com.java.volunteer.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic DAO interface with basic CRUD operations
 * 
 * @param <T> the entity type
 * @param <ID> the primary key type
 */
public interface BaseDao<T, ID> {
    
    /**
     * Save an entity to the database
     * 
     * @param entity the entity to save
     * @return the saved entity with its ID
     * @throws SQLException if a database error occurs
     */
    T save(T entity) throws SQLException;
    
    /**
     * Find an entity by its ID
     * 
     * @param id the entity ID
     * @return the entity or null if not found
     * @throws SQLException if a database error occurs
     */
    T findById(ID id) throws SQLException;
    
    /**
     * Find all entities
     * 
     * @return a list of all entities
     * @throws SQLException if a database error occurs
     */
    List<T> findAll() throws SQLException;
    
    /**
     * Update an existing entity
     * 
     * @param entity the entity to update
     * @return the updated entity
     * @throws SQLException if a database error occurs
     */
    T update(T entity) throws SQLException;
    
    /**
     * Delete an entity by its ID
     * 
     * @param id the entity ID
     * @return true if the entity was deleted, false otherwise
     * @throws SQLException if a database error occurs
     */
    boolean deleteById(ID id) throws SQLException;
}