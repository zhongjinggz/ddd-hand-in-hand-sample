package qiyebao.application.orgmng.empservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.emp.*;

import java.util.List;

@Service
public class EmpService {
    private final EmpRepository empRepository;
    private final EmpBuilderFactory builderFactory;
    private final EmpHandler handler;

    @Autowired
    public EmpService(EmpRepository empRepository
        , EmpBuilderFactory builderFactory
        , EmpHandler handler
    ) {
        this.empRepository = empRepository;
        this.builderFactory = builderFactory;
        this.handler = handler;
    }

    @Transactional
    public EmpResponse addEmp(addEmpRequest request, Long userId) {
        EmpBuilder builder = builderFactory.newBuilder()
            .tenantId(request.getTenantId())
            .orgId(request.getOrgId())
            .idNum(request.getIdNum())
            .dob(request.getDob())
            .name(request.getName())
            .genderCode(request.getGenderCode())
            .statusCode(request.getStatusCode())
            .createdBy(userId);

        for (var skill : request.getSkills()) {
            builder.addSkill(
                skill.getSkillTypeId(),
                skill.getLevelCode(),
                skill.getDuration());
        }

        for (var exp : request.getExperiences()) {
            builder.addExperience(
                exp.getStartDate(),
                exp.getEndDate(),
                exp.getCompany());
        }

        for (var post : request.getPosts()) {
            builder.addPost(post);
        }

        Emp emp = builder.build();

        empRepository.add(emp);
        return new EmpResponse(emp);
    }

    @Transactional
    public EmpResponse modifyEmp(Long empId, ModifyEmpRequest request, Long userId) {
        Emp emp = empRepository.findById(request.getTenantId(), empId)
            .orElseThrow(() -> new BusinessException(
                "Emp id(" + empId + ") 不正确！"));

        handler.modifyRoot(emp
            , request.getIdNum()
            , request.getName()
            , request.getDob()
            , Gender.ofCode(request.getGenderCode())
            , userId
        );

        modifySkills(emp, request.getSkills(), userId);
        modifyExperiences(emp, request.getExperiences(), userId);
        modifyPosts(emp, request.getPosts(), userId);

        empRepository.save(emp);
        return new EmpResponse(emp);
    }

    private boolean modifySkills(Emp emp, List<SkillDto> requestSkills, Long userId) {
        return false;
    }

    private boolean modifyExperiences(Emp emp, List<WorkExperienceDto> requestExps, Long userId) {
        return false;
    }

    private boolean modifyPosts(Emp emp, List<String> requestPosts, Long userId) {
        return false;
    }
}
