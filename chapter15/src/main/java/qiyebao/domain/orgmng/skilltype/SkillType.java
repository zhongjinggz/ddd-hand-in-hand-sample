package qiyebao.domain.orgmng.skilltype;

import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.CodeEnum;
import qiyebao.common.framework.domain.PersistentStatus;

import java.time.LocalDateTime;

public class SkillType extends AuditableEntity {
    private Long tenantId;
    private Long id;
    private String name;

    public SkillType(Long tenantId
        , Long id
        , String name
        , LocalDateTime createdAt
        , Long createdBy) {
        super(PersistentStatus.NEW, createdAt, createdBy);
        this.tenantId = tenantId;
        this.id = id;
        this.name = name;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static enum Status implements CodeEnum {
        EFFECTIVE("EF", "有效"),
        TERMINATED("TE", "终止");

        private final String code;
        private final String desc;

        private Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static SkillType.Status ofCode(String code) {
            return CodeEnum.ofCode(values(), code);
        }

        @Override
        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
