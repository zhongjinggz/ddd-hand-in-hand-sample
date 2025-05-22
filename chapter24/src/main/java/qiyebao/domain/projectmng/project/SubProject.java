package qiyebao.domain.projectmng.project;

import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.domain.effortmng.effortitem.EffortItem;

import java.time.LocalDateTime;

public class SubProject extends AuditableEntity
        implements EffortItem {

    private final Long tenantId;
    private final Long effortItemId;
    private String name;

    private Status status;

    public SubProject(Long tenantId
            , Long effortItemId
            , String name
            , Long createdBy) {

        super(PersistentStatus.NEW, LocalDateTime.now(), createdBy);
        this.tenantId = tenantId;
        this.effortItemId = effortItemId;
        this.name = name;
        this.status = Status.TODO;
    }

    @Override
    public Long getEffortItemId() {
        return effortItemId;
    }

    @Override
    public String getName() {
        return name;
    }

    public String setName() {
        return this.name = name;
    }

    public static enum Status {
        TODO, DOING, DONE;
    }
}
