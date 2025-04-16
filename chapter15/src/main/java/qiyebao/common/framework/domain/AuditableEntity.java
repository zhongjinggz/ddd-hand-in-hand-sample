package qiyebao.common.framework.domain;

import java.time.LocalDateTime;

public abstract class AuditableEntity {
    protected PersistentStatus persistentStatus;
    protected LocalDateTime createdAt;
    protected Long createdBy;
    protected LocalDateTime updatedAt;
    protected Long updatedBy;

    public AuditableEntity() {}

    public AuditableEntity(PersistentStatus persistentStatus
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        this.persistentStatus = persistentStatus;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public void toAsIs () {
        this.persistentStatus = PersistentStatus.AS_IS;
    }

    public PersistentStatus getPersistentStatus() {
        return persistentStatus;
    }

    public void toNew () {
        this.persistentStatus = PersistentStatus.NEW;
    }

    public void toDeleted () {
        this.persistentStatus = PersistentStatus.DELETED;
    }

    public void toUpdated() {
        this.persistentStatus = PersistentStatus.UPDATED;
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
