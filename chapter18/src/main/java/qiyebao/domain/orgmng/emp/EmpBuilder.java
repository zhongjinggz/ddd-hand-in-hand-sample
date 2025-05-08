package qiyebao.domain.orgmng.emp;

import qiyebao.domain.common.valueobject.DatePeriod;
import qiyebao.domain.orgmng.emp.validator.EmpValidators;
import qiyebao.domain.orgmng.empnumcounter.EmpNumCounterRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmpBuilder {
    final EmpValidators expect;
    final EmpNumCounterRepository empNumCounterRepository;

    private Long tenantId;
    private Long orgId;
    private String idNum;
    private LocalDate dob;
    private String name;
    private String genderCode;
    private String statusCode;
    private Long createdBy;

    private final List<Map<String, Object>> skills = new ArrayList<>();
    private final List<Map<String, Object>> experiences = new ArrayList<>();
    private final List<String> posts = new ArrayList<>();

    EmpBuilder(EmpValidators expect
        , EmpNumCounterRepository empNumCounterRepository) {
        this.expect = expect;
        this.empNumCounterRepository = empNumCounterRepository;
    }

    public EmpBuilder tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public EmpBuilder orgId(Long orgId) {
        this.orgId = orgId;
        return this;
    }

    public EmpBuilder idNum(String idNum) {
        this.idNum = idNum;
        return this;
    }

    public EmpBuilder dob(LocalDate dob) {
        this.dob = dob;
        return this;
    }

    public EmpBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EmpBuilder genderCode(String genderCode) {
        this.genderCode = genderCode;
        return this;
    }

    public EmpBuilder statusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public EmpBuilder createdBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String generateEmpNum(Long tenantId) {
        int yearNum = LocalDate.now().getYear();
        int maxNum = empNumCounterRepository.nextNumByYear(tenantId, yearNum);
        return (String.format("%04d%08d", yearNum, maxNum));
    }

    public EmpBuilder addSkill(Long skillTypeId, String levelCode, Integer duration) {
        skills.add(Map.of(
            "skillTypeId", skillTypeId
            , "levelCode", levelCode
            , "duration", duration));
        return this;
    }

    public EmpBuilder addExperience(LocalDate startDate, LocalDate endDate, String company) {
        experiences.add(Map.of(
            "startDate", startDate
            , "endDate", endDate
            , "company", company));
        return this;
    }

    public EmpBuilder addPost(String postTypeCode) {
        posts.add(postTypeCode);
        return this;
    }

    public Emp build() {
        validate();

        Emp result = new Emp(tenantId, statusCode, createdBy);
        result.setEmpNum(generateEmpNum(tenantId));
        result.setIdNum(idNum);
        result.setDob(dob);
        result.setOrgId(orgId);
        result.setName(name);
        result.setGender(Gender.ofCode(genderCode));


        skills.forEach(s ->
            result.addSkill(
                (Long) s.get("skillTypeId")
                , Skill.Level.ofCode((String) s.get("levelCode"))
                , (Integer) s.get("duration")
                , createdBy)
        );

        experiences.forEach(e -> result.addExperience(
            DatePeriod.of((LocalDate) e.get("startDate"), (LocalDate) e.get("endDate"))
            , (String) e.get("company")
            , createdBy)
        );

        posts.forEach((p -> result.addPost(p, createdBy)));

        return result;
    }

    public void validate() {
        validateCommonInfo();
        validateOrg();
        validateSkills();
        validatePosts();
    }

    private void validateCommonInfo() {
        expect.tenant().shouldValid(tenantId);
        expect.user().shouldValid(tenantId, createdBy);
    }

    private void validateOrg() {
        expect.org().shouldValid(tenantId, orgId);
    }

    private void validateSkills() {
        for (Map<String, Object> skill : skills) {
            expect.skill().typeShouldValid(tenantId, (Long) skill.get("skillTypeId"));
        }
    }

    private void validatePosts() {
        for (String postTypeCode : posts) {
            expect.post().typeShouldValid(tenantId, postTypeCode);
        }
    }
}
