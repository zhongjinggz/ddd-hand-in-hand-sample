package qiyebao.domain.orgmng.emp;


import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.domain.common.valueobject.DatePeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class WorkExperience extends AuditableEntity {
    private Emp emp;
    final private Long tenantId;
    final private DatePeriod period;
    private String company;

    WorkExperience(Emp emp
        , Long tenantId
        , DatePeriod period
        , LocalDateTime createdAt
        , Long createdBy) {

        super(PersistentStatus.NEW, createdAt, createdBy);
        this.emp = emp;
        this.tenantId = tenantId;
        this.period = period;
    }

    public Emp getEmp() {
        return emp;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public DatePeriod getPeriod(){
        return period;
    }


    public String getCompany() {
        return company;
    }

    void setCompany(String company) {
        this.company = company;
    }
}
