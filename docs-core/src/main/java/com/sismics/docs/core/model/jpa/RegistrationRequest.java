package com.sismics.docs.core.model.jpa;

import com.google.common.base.MoreObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * Guest registration request entity.
 * 
 * @author jianfa
 */
@Entity
@Table(name = "T_REGISTRATION_REQUEST")
public class RegistrationRequest {
    /**
     * Identifier.
     */
    @Id
    @Column(name = "REQ_ID_C", length = 36)
    private String id;

    /**
     * Username requested.
     */
    @Column(name = "REQ_USERNAME_C", nullable = false, length = 50)
    private String username;

    /**
     * Email address.
     */
    @Column(name = "REQ_EMAIL_C", nullable = false, length = 100)
    private String email;

    /**
     * Reason for request.
     */
    @Column(name = "REQ_REASON_C", length = 1000)
    private String reason;

    /**
     * Status: PENDING / ACCEPTED / REJECTED.
     */
    @Column(name = "REQ_STATUS_C", nullable = false, length = 20)
    private String status;

    /**
     * Create date.
     */
    @Column(name = "REQ_CREATEDATE_D", nullable = false)
    private Date createDate;

    /**
     * Admin processed date.
     */
    @Column(name = "REQ_PROCESSEDDATE_D")
    private Date processedDate;

    /**
     * Whether approved.
     */
    @Column(name = "REQ_APPROVED_B")
    private Boolean approved;

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("email", email)
                .add("status", status)
                .toString();
    }
}
