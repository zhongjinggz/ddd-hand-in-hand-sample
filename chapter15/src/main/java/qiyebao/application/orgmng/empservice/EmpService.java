package qiyebao.application.orgmng.empservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.emp.*;

import java.util.*;

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

        handler.modifyIdNum(emp, request.getIdNum());
        handler.modifyName(emp, request.getName());
        handler.modifyDob(emp, request.getDob());
        handler.modifyGender(emp, Gender.ofCode(request.getGenderCode()));

        modifySkills(emp, request.getSkills(), userId);
        modifyExperiences(emp, request.getExperiences(), userId);
        modifyPosts(emp, request.getPosts(), userId);

        handler.setUpdatedInfo(emp, userId);

        empRepository.save(emp);
        return new EmpResponse(emp);
    }

    private void modifySkills(Emp emp, List<SkillDto> requestSkills, Long userId) {
        Collection<SkillDto> requestCopy = new ArrayList<>(requestSkills); // 创建 request 的副本

        for (Skill currentSkill : emp.getSkills()) {
            boolean found = false;
            Iterator<SkillDto> requestIterator = requestCopy.iterator();
            while (requestIterator.hasNext()) {
                SkillDto requestSkill = requestIterator.next();
                if (Objects.equals(currentSkill.getSkillTypeId()
                    , requestSkill.getSkillTypeId())
                ) {
                    handler.modifySkill(emp
                        , requestSkill.getSkillTypeId()
                        , Skill.Level.ofCode(requestSkill.getLevelCode())
                        , requestSkill.getDuration()
                        , userId);
                    found = true;
                    requestIterator.remove(); // 删除 request 中的相应元素
                    break;
                }
            }
            if (!found) {
                handler.removeSkill(emp
                    , currentSkill.getSkillTypeId()
                    , userId);
            }
        }

        for (SkillDto requestSkill : requestCopy) {
            handler.addSkill(emp
                , requestSkill.getSkillTypeId()
                , Skill.Level.ofCode(requestSkill.getLevelCode())
                , requestSkill.getDuration()
                , userId);
        }
    }

    private boolean modifyExperiences(Emp emp, List<WorkExperienceDto> requestExps, Long userId) {
        // 类似 modifySkills
        return false;
    }

    private boolean modifyPosts(Emp emp, List<String> requestPosts, Long userId) {
        // 类似 modifySkills
        return false;
    }
}
