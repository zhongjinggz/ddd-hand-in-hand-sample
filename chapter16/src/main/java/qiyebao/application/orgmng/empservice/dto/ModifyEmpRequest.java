package qiyebao.application.orgmng.empservice.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModifyEmpRequest {
    private Long tenantId;
    private String idNum;
    private String empNum;
    private String name;
    private String genderCode;
    private LocalDate dob;

    protected List<SkillDto> skills = new ArrayList<>();
    protected List<WorkExperienceDto> experiences = new ArrayList<>();
    protected List<String> posts = new ArrayList<>();


    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getEmpNum() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
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

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    public List<WorkExperienceDto> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<WorkExperienceDto> experiences) {
        this.experiences = experiences;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

}
