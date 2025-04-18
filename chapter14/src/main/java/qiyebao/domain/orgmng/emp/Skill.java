package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.CodeEnum;

import java.time.LocalDate;
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
        super(createdAt, createdBy);
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

    Skill setLevel(Level level) {
        this.level = level;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    Skill setDuration(Integer duration) {
        this.duration = duration;
        return this;
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
