package qiyebao.domain.orgmng.emp;


import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.PersistentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class WorkExperience extends AuditableEntity {
    private Emp emp;
    final private Long tenantId;
    final private LocalDate startDate;
    final private LocalDate endDate;
    private String company;

    WorkExperience(Emp emp
        , Long tenantId
        , LocalDate startDate
        , LocalDate endDate
        , LocalDateTime createdAt
        , Long createdBy) {

        super(PersistentStatus.NEW, createdAt, createdBy);
        this.emp = emp;
        this.tenantId = tenantId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Emp getEmp() {
        return emp;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getCompany() {
        return company;
    }

    void setCompany(String company) {
        this.company = company;
    }
}
