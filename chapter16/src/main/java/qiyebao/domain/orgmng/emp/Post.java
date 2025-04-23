package qiyebao.domain.orgmng.emp;


import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.PersistentStatus;

import java.time.LocalDateTime;


public class Post extends AuditableEntity {
    final private Emp emp;
    final private Long tenantId;
    final private String postTypeCode;

    Post(Emp emp
        , Long tenantId
        , String postTypeCode
        , LocalDateTime createdAt
        , Long createdBy) {

        super(PersistentStatus.NEW, createdAt, createdBy);
        this.emp = emp;
        this.tenantId = tenantId;
        this.postTypeCode = postTypeCode;
    }

    public Emp getEmp() {
        return emp;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getPostTypeCode() {
        return postTypeCode;
    }
}
