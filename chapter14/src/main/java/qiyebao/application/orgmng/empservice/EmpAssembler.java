package qiyebao.application.orgmng.empservice;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.Gender;
import qiyebao.domain.orgmng.emp.Skill;
import qiyebao.domain.orgmng.emp.validator.EmpValidators;
import qiyebao.domain.orgmng.empnumcounter.EmpNumCounterRepository;

import java.time.LocalDate;
import java.util.List;

@Component
public class EmpAssembler {
    final private EmpValidators expect;
    final private EmpNumCounterRepository empNumCounterRepository;

    EmpAssembler(EmpValidators expect
        , EmpNumCounterRepository empNumCounterRepository
    ) {
        this.expect = expect;
        this.empNumCounterRepository = empNumCounterRepository;
    }

    Emp buildEmp(addEmpRequest request, Long userId) {
        validate(request, userId);

        Emp result = new Emp(request.getTenantId()
            , Emp.Status.ofCode(request.getStatusCode())
            , userId);
        result.setEmpNum(generateEmpNum(request.getTenantId()));
        result.setIdNum(request.getIdNum());
        result.setDob(request.getDob());
        result.setOrgId(request.getOrgId());
        result.setName(request.getName());
        result.setGender(Gender.ofCode(request.getGenderCode()));


        for (SkillDto skill : request.getSkills()) {
            result.addSkill(skill.getSkillTypeId()
                , Skill.Level.ofCode(skill.getLevelCode())
                , skill.getDuration()
                , userId);
        }

        for (WorkExperienceDto exp : request.getExperiences()) {
            result.addExperience(exp.getStartDate()
                , exp.getEndDate()
                , exp.getCompany()
                , userId);
        }

        for (String postCode : request.getPosts()) {
            result.addPost(postCode, userId);
        }

        return result;
    }

    private void validate(addEmpRequest request, Long userId) {
        validateCommonInfo(request.getTenantId(), userId);
        validateOrg(request.getTenantId(), request.getOrgId());
        validateSkills(request.getTenantId(), request.getSkills());
        validatePosts(request.getTenantId(), request.getPosts());
    }

    private void validateCommonInfo(Long tenantId, Long userId) {
        expect.tenant().shouldValid(tenantId);
        expect.user().shouldValid(tenantId, userId);
    }

    private void validateOrg(Long tenantId, Long orgId) {
        expect.org().shouldValid(tenantId, orgId);
    }

    private void validateSkills(Long tenantId, List<SkillDto> skills) {
        for (var skill : skills) {
            expect.skill().typeShouldValid(tenantId, skill.getSkillTypeId());
        }
    }

    private void validatePosts(Long tenantId, List<String> posts) {
        for (String postTypeCode : posts) {
            expect.post().typeShouldValid(tenantId, postTypeCode);
        }
    }

    private String generateEmpNum(Long tenantId) {
        int yearNum = LocalDate.now().getYear();
        int maxNum = empNumCounterRepository.nextNumByYear(tenantId, yearNum);
        return (String.format("%04d%08d", yearNum, maxNum));
    }
}
