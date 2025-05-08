package qiyebao.application.orgmng.empservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qiyebao.application.orgmng.empservice.dto.EmpResponse;
import qiyebao.application.orgmng.empservice.dto.ModifyEmpRequest;
import qiyebao.application.orgmng.empservice.dto.addEmpRequest;
import qiyebao.application.orgmng.empservice.modifier.ExperiencesModifier;
import qiyebao.application.orgmng.empservice.modifier.PostsModifier;
import qiyebao.application.orgmng.empservice.modifier.SkillsModifier;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.emp.*;

@Service
public class EmpService {
    private final EmpRepository empRepository;
    private final EmpBuilderFactory builderFactory;
    private final EmpHandler handler;
    private final SkillsModifier skillsModifier;
    private final ExperiencesModifier experiencesModifier;
    private final PostsModifier postsModifier;

    @Autowired
    public EmpService(EmpRepository empRepository
        , EmpBuilderFactory builderFactory
        , EmpHandler handler
        , SkillsModifier skillsModifier
        , ExperiencesModifier experiencesModifier
        , PostsModifier postsModifier
    ) {
        this.empRepository = empRepository;
        this.builderFactory = builderFactory;
        this.handler = handler;
        this.skillsModifier = skillsModifier;
        this.experiencesModifier = experiencesModifier;
        this.postsModifier = postsModifier;
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

        empRepository.save(emp);
        return new EmpResponse(emp);
    }

    @Transactional
    public EmpResponse becomeRegular(Long tenantId, Long empId, Long userId) {
        Emp emp = empRepository.findById(tenantId, empId)
            .orElseThrow(() -> new BusinessException(
                "Emp id(" + empId + ") 不正确！"));

        handler.becomeRegular(emp);
        handler.setUpdatedInfo(emp, userId);

        empRepository.save(emp);
        return new EmpResponse(emp);
    }

    @Transactional
    public EmpResponse terminate(Long tenantId, Long empId, Long userId) {
        Emp emp = empRepository.findById(tenantId, empId)
            .orElseThrow(() -> new BusinessException(
                "Emp id(" + empId + ") 不正确！"));

        handler.terminate(emp);
        handler.setUpdatedInfo(emp, userId);

        empRepository.save(emp);
        return new EmpResponse(emp);
    }

    @Transactional
    public EmpResponse modifyEmp(Long empId, ModifyEmpRequest request, Long userId) {
        Emp emp = empRepository.findById(request.getTenantId(), empId)
            .orElseThrow(() -> new BusinessException(
                "Emp id(" + empId + ") 不正确！"));

        handler.modifyIdNum(emp, request.getIdNum());
        handler.modifyName(emp, request.getName());
        handler.modifyDob(emp, request.getDob());
        handler.modifyGender(emp, Gender.ofCode(request.getGenderCode()));

        skillsModifier.modify(emp, request.getSkills(), userId);
        experiencesModifier.modify(emp, request.getExperiences(), userId);
        postsModifier.modify(emp, request.getPosts(), userId);

        handler.setUpdatedInfo(emp, userId);

        empRepository.save(emp);
        return new EmpResponse(emp);
    }
}
