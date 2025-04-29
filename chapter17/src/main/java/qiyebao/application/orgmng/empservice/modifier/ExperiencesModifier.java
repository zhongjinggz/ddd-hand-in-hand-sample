package qiyebao.application.orgmng.empservice.modifier;

import org.springframework.stereotype.Component;
import qiyebao.application.orgmng.empservice.dto.WorkExperienceDto;
import qiyebao.common.framework.domain.CollectionModifier;
import qiyebao.domain.common.valueobject.DatePeriod;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpHandler;
import qiyebao.domain.orgmng.emp.WorkExperience;

import java.util.Collection;
import java.util.Objects;

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
        return Objects.equals(currExperience.getPeriod(),
            DatePeriod.of(reqExperience.getStartDate(), reqExperience.getEndDate())
        );
    }

    @Override
    protected void modifyItem(Emp emp, WorkExperienceDto reqExperience, Long userId) {
        handler.modifyExperience(emp
            , DatePeriod.of(reqExperience.getStartDate(), reqExperience.getEndDate())
            , reqExperience.getCompany()
            , userId
        );
    }

    @Override
    protected void removeItem(Emp emp, WorkExperience currExperience, Long userId) {
        handler.removeExperience(emp
            , currExperience.getPeriod()
            , userId);
    }

    @Override
    protected void addItem(Emp emp, WorkExperienceDto reqExperience, Long userId) {
        handler.addExperience(emp
            , DatePeriod.of(reqExperience.getStartDate(), reqExperience.getEndDate())
            , reqExperience.getCompany()
            , userId
        );
    }
}