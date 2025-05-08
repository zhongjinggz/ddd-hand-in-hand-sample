package qiyebao.domain.orgmng.org;

import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.CodeEnum;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.common.framework.exception.BusinessException;

import java.time.LocalDateTime;

public class Org extends AuditableEntity {
    private Long id;
    private Long tenantId;
    private Long superiorId;
    private String orgTypeCode;
    private Long leaderId;
    private String name;
    private Status status;

    public Org (Long id
        , Long tenantId
        , String orgTypeCode
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        super(PersistentStatus.NEW, createdAt, createdBy);
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //Org 管理自己的状态
    public void cancel() {
        shouldEffective();   // 校验组织状态的业务规则移至对象内部
        this.status = Status.CANCELLED;
    }

    // 要撤销的组织必须是生效状态
    public void shouldEffective() {
        if (!(status == Status.EFFECTIVE)) {
            throw new BusinessException("该组织不是有效状态，不能撤销！");
        }
    }

    public static enum Status implements CodeEnum {
        EFFECTIVE("EF", "有效"),
        CANCELLED("CA", "终止");

        private final String code;
        private final String desc;

        Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }


        public static Status ofCode(String code) {
            return CodeEnum.ofCode(values(), code);
        }
    }
}
