package qiyebao.common.framework.domain;

import java.time.LocalDateTime;

public abstract class AuditableEntity extends Persistent {
    protected LocalDateTime createdAt;
    protected Long createdBy;
    protected LocalDateTime updatedAt;
    protected Long updatedBy;

    public AuditableEntity() {}

    public AuditableEntity(PersistentStatus persistentStatus
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        super(persistentStatus);
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public void setUpdatedInfo(Long userId) {
        if(getPersistentStatus() == PersistentStatus.UPDATED) {
            this.updatedAt = LocalDateTime.now();
            this.updatedBy = userId;
        }
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }
}
