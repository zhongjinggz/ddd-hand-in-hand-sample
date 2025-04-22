package qiyebao.application.orgmng.empservice.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class addEmpRequest {
    private Long tenantId;
    private Long orgId;
    private String idNum;
    private String name;
    private String genderCode;
    private LocalDate dob;
    private String statusCode;
    protected List<SkillDto> skills = new ArrayList<>();
    protected List<WorkExperienceDto> experiences = new ArrayList<>();
    protected List<String> posts = new ArrayList<>();

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    public void addSkill(Long skillTypeId, String levelCode, Integer duration) {
        this.skills.add(new SkillDto(skillTypeId, levelCode, duration));
    }

    public List<WorkExperienceDto> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<WorkExperienceDto> experiences) {
        this.experiences = experiences;
    }

    public void addExperience(LocalDate startDate, LocalDate endDate, String company) {
        this.experiences.add(new WorkExperienceDto(startDate
            , endDate, company));
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public void addPost(String postCode) {
        this.posts.add(postCode);
    }
}
