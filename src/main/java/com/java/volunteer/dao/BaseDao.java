package com.java.volunteer.dao;

import java.util.List;
import java.util.Optional;

/**
 * Base DAO interface for CRUD operations
 * @param <T> entity type
 * @param <ID> primary key type
 */
public interface BaseDao<T, ID> {
    /**
     * Create a new entity
     * @param entity the entity to create
     * @return the created entity with ID assigned
     */
    T create(T entity);
    
    /**
     * Find an entity by its ID
     * @param id the entity ID
     * @return an Optional containing the entity if found, or empty if not found
     */
    Optional<T> findById(ID id);
    
    /**
     * Find all entities
     * @return a list of all entities
     */
    List<T> findAll();
    
    /**
     * Update an existing entity
     * @param entity the entity to update
     * @return the updated entity
     */
    T update(T entity);
    
    /**
     * Delete an entity by ID
     * @param id the entity ID to delete
     * @return true if the entity was deleted, false otherwise
     */
    boolean deleteById(ID id);
}