package qiyebao.application.orgmng.empservice.dto;


import qiyebao.domain.orgmng.emp.Skill;

public class SkillDto {
    private Long skillTypeId;
    String levelCode;  //BEG-Beginner MED-Medium ADV-Advanced
    private Integer duration;

    public SkillDto() {}

    public SkillDto(Long skillTypeId, String levelCode, Integer duration) {
        this.skillTypeId = skillTypeId;
        this.levelCode = levelCode;
        this.duration = duration;
    }

    public SkillDto(Skill skill) {
        this(skill.getSkillTypeId()
            , skill.getLevel().getCode()
            , skill.getDuration());
    }

    public Long getSkillTypeId() {
        return skillTypeId;
    }

    public void setSkillTypeId(Long skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
