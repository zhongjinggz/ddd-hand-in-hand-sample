package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.CodeEnum;
import qiyebao.common.framework.domain.PersistentStatus;

import java.time.LocalDateTime;

public class Skill extends AuditableEntity {
    private Emp emp;
    final private Long tenantId;
    final private Long skillTypeId;
    protected Level level;
    protected Integer duration;

    Skill(Emp emp
        , Long tenantId
        , Long skillTypeId
        , LocalDateTime createdAt
        , Long createdBy) {
        super(PersistentStatus.NEW, createdAt, createdBy);
        this.emp = emp;
        this.tenantId = tenantId;
        this.skillTypeId = skillTypeId;
    }


    public Emp getEmp() {
        return emp;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Long getSkillTypeId() {
        return skillTypeId;
    }

    public Level getLevel() {
        return level;
    }

    void setLevel(Level level) {
        this.level = level;
    }

    public Integer getDuration() {
        return duration;
    }

    void setDuration(Integer duration) {
        this.duration = duration;
    }

    public static enum Level implements CodeEnum {
        BEGINNER("BEG", "初级"), MEDIUM("MED", "中级"), ADVANCED("ADV", "高级");

        private final String code;
        private final String desc;

        public static Level ofCode(String code) {
            return CodeEnum.ofCode(values(), code);
        }

        Level(String code, String desc) {
            this.code = code;
            this.desc = desc;
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
