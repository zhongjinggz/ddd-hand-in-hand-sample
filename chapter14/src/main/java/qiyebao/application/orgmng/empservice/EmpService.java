package qiyebao.application.orgmng.empservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpBuilder;
import qiyebao.domain.orgmng.emp.EmpBuilderFactory;
import qiyebao.domain.orgmng.emp.EmpRepository;

@Service
public class EmpService {
    private final EmpRepository empRepository;
    private final EmpBuilderFactory builderFactory;

    @Autowired
    public EmpService(EmpRepository empRepository
            , EmpBuilderFactory builderFactory
    ) {
        this.empRepository = empRepository;
        this.builderFactory = builderFactory;
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

        for ( var skill : request.getSkills()) {
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

}
