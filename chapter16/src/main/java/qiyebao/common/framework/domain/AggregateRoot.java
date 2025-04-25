package qiyebao.common.framework.domain;

import java.time.LocalDateTime;

public class AggregateRoot extends AuditableEntity {
    private Long version;

    public AggregateRoot(PersistentStatus persistentStatus
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        super(persistentStatus, createdAt, createdBy);
        this.version = 0L;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
