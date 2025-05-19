package com.sismics.docs.core.service;

import com.sismics.docs.core.dao.RegistrationRequestDao;
import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.model.jpa.RegistrationRequest;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.util.EncryptionUtil;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.persistence.EntityManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing guest registration requests.
 * Handles submission, approval, and rejection of registration requests.
 * 
 * @author jianfa
 */
public class RegistrationRequestService {
    private final EntityManager entityManager;

    public RegistrationRequestService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Submits a new guest registration request.
     * 
     * @param username Desired username
     * @param email Guest email
     * @param reason Reason for registration
     */
    public void submitRequest(String username, String email, String reason) {
        RegistrationRequest request = new RegistrationRequest();
        request.setId(UUID.randomUUID().toString());
        request.setUsername(username);
        request.setEmail(email);
        request.setReason(reason);
        request.setStatus("PENDING");
        request.setCreateDate(new Date());

        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(request);
    }

    /**
     * Returns a list of all pending registration requests.
     * 
     * @return List of requests
     */
    public List<RegistrationRequest> getPendingRequests() {
        return new RegistrationRequestDao(entityManager).findAllPending();
    }

    /**
     * Approves a pending registration request.
     * 
     * @param id Request ID
     * @return true if approved successfully
     */
    public boolean approveRequest(String id) {
        RegistrationRequestDao dao = new RegistrationRequestDao(entityManager);
        RegistrationRequest request = dao.findById(id);
        if (request != null && "PENDING".equals(request.getStatus())) {
            request.setStatus("ACCEPTED");
            request.setApproved(true);
            request.setProcessedDate(new Date());
            dao.update(request);

            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPassword("changeme"); // 明文，后续建议改为哈希
            newUser.setRoleId("user");
            newUser.setPrivateKey(EncryptionUtil.generatePrivateKey());
            newUser.setStorageQuota(100_000L); // 默认配额
            newUser.setStorageCurrent(0L);
            newUser.setCreateDate(new Date());
            newUser.setOnboarding(true);

            try {
                UserDao userDao = new UserDao();
                userDao.create(newUser, "admin");
            } catch (Exception e) {
                e.printStackTrace(); // 你可以改为 log 或上抛
                return false;
        }

            return true;
        }
        return false;
    }

    /**
     * Rejects a pending registration request.
     * 
     * @param id Request ID
     * @return true if rejected successfully
     */
    public boolean rejectRequest(String id) {
        RegistrationRequestDao dao = new RegistrationRequestDao(entityManager);
        RegistrationRequest request = dao.findById(id);
        if (request != null && "PENDING".equals(request.getStatus())) {
            request.setStatus("REJECTED");
            request.setApproved(false);
            request.setProcessedDate(new Date());
            dao.update(request);
            return true;
        }
        return false;
    }
}
