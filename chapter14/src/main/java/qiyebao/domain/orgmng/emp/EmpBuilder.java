package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.exception.BusinessException;
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

    public EmpBuilder addPostCode(String postCode) {
        posts.add(postCode);
        return this;
    }

    public Emp build() {
        return null;
    }

    public void validate() {
        validateCommonInfo();
        validateOrg();
        validateIdNum();
        validateDob();
        validateName();
        validateGender();
        validateStatus();
    }

    private void validateStatus() {
        if (!Emp.Status.isValidCode(statusCode)) {
           throw new BusinessException("无效的状态代码");
        }
    }

    private void validateGender() {

    }

    private void validateName() {

    }

    private void validateDob() {
        
    }

    private void validateIdNum() {
        
    }

    private void validateCommonInfo() {
        expect.tenant().shouldValid(tenantId);
        expect.user().shouldValid(tenantId, createdBy);
    }

    private void validateOrg() {
        expect.org().shouldValid(tenantId, orgId);
    }
}
