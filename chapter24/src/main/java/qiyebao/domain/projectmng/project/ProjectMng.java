package qiyebao.domain.projectmng.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.domain.common.valueobject.DatePeriod;

public class ProjectMng extends AuditableEntity {
    private final Long tenantId;
    private DatePeriod period;
    private final Long empId;
    private Status status;

    public ProjectMng(Long tenantId
            , LocalDate startAt
            , Long empId
            , Long createdBy) {

        super(PersistentStatus.NEW, LocalDateTime.now(), createdBy);
        this.tenantId = tenantId;
        this.period = DatePeriod.startAt(startAt);
        this.empId = empId;
        this.status = Status.EFFECTIVE;
    }

    void terminate(LocalDate endAt) {
        this.status = Status.TERMINATED;
        this.period = DatePeriod.of(this.period.getStart(), endAt);
    }

    public enum Status {
        EFFECTIVE, TERMINATED
    }
}
