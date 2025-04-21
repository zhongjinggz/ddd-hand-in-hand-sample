package qiyebao.application.orgmng.empservice;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.domain.CollectionModifier;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpHandler;
import qiyebao.domain.orgmng.emp.WorkExperience;

import java.util.Collection;

@Component
public class ExperiencesModifier extends CollectionModifier<Emp, WorkExperience, WorkExperienceDto> {
    private final EmpHandler handler;

    ExperiencesModifier(EmpHandler handler) {
        this.handler = handler;
    }

    @Override
    protected Collection<WorkExperience> getCurrItems(Emp emp) {
        return emp.getExperiences();
    }

    @Override
    protected boolean isSame(WorkExperience currExperience, WorkExperienceDto reqExperience) {
        return currExperience.getStartDate().equals(reqExperience.getStartDate())
            && currExperience.getEndDate().equals(reqExperience.getEndDate());
    }

    @Override
    protected void modifyItem(Emp emp, WorkExperienceDto reqExperience, Long userId) {
        handler.modifyExperience(emp
            , reqExperience.getStartDate()
            , reqExperience.getEndDate()
            , reqExperience.getCompany()
            , userId
        );
    }

    @Override
    protected void removeItem(Emp emp, WorkExperience currExperience, Long userId) {
        handler.removeExperience(emp
            , currExperience.getStartDate()
            , currExperience.getEndDate()
            , userId);
    }

    @Override
    protected void addItem(Emp emp, WorkExperienceDto reqExperience, Long userId) {
        handler.addExperience(emp
            , reqExperience.getStartDate()
            , reqExperience.getEndDate()
            , reqExperience.getCompany()
            , userId
        );
    }
}