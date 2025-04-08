package qiyebao.domain.orgmng.org;

import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.exception.BusinessException;

import java.time.LocalDateTime;

public class Org extends AuditableEntity {
    private Long id;
    private Long tenantId;
    private Long superiorId;
    private String orgTypeCode;
    private Long leaderId;
    private String name;
    private OrgStatus status;

    public Org (Long id
        , Long tenantId
        , String orgTypeCode
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        super(createdAt, createdBy);
        this.tenantId = tenantId;
        this.orgTypeCode = orgTypeCode;
    }

    public Long getId() {
        return id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getOrgTypeCode() {
        return orgTypeCode;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrgStatus getStatus() {
        return status;
    }

    public void setStatus(OrgStatus status) {
        this.status = status;
    }

    //Org 管理自己的状态
    public void cancel() {
        shouldEffective();   // 校验组织状态的业务规则移至对象内部
        this.status = OrgStatus.CANCELLED;
    }

    // 要撤销的组织必须是生效状态
    public void shouldEffective() {
        if (!(status == OrgStatus.EFFECTIVE)) {
            throw new BusinessException("该组织不是有效状态，不能撤销！");
        }
    }
}
