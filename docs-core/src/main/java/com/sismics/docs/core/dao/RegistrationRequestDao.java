package com.sismics.docs.core.dao;

import com.sismics.docs.core.model.jpa.RegistrationRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * DAO for guest registration requests.
 * Handles persistence operations for RegistrationRequest entities.
 * 
 * @author jianfa
 */
public class RegistrationRequestDao {
    private final EntityManager entityManager;

    public RegistrationRequestDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Creates a new registration request.
     * 
     * @param request Registration request to persist
     */
    public void create(RegistrationRequest request) {
        entityManager.persist(request);
    }

    /**
     * Updates an existing registration request.
     * 
     * @param request Registration request to update
     */
    public void update(RegistrationRequest request) {
        entityManager.merge(request);
    }

    /**
     * Finds a registration request by its ID.
     * 
     * @param id Request ID
     * @return RegistrationRequest if found, null otherwise
     */
    public RegistrationRequest findById(String id) {
        return entityManager.find(RegistrationRequest.class, id);
    }

    /**
     * Lists all pending registration requests.
     * 
     * @return List of pending requests
     */
    public List<RegistrationRequest> findAllPending() {
        TypedQuery<RegistrationRequest> query = entityManager.createQuery(
            "SELECT r FROM RegistrationRequest r WHERE r.status = 'PENDING'", RegistrationRequest.class);
        return query.getResultList();
    }
}
