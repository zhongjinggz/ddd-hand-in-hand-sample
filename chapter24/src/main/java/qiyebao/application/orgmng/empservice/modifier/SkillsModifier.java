package qiyebao.application.orgmng.empservice.modifier;

import org.springframework.stereotype.Component;
import qiyebao.application.orgmng.empservice.dto.SkillDto;
import qiyebao.common.framework.domain.CollectionModifier;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpHandler;
import qiyebao.domain.orgmng.emp.Skill;

import java.util.Collection;
import java.util.Objects;

@Component
public class SkillsModifier extends CollectionModifier<Emp, Skill, SkillDto> {
    private final EmpHandler handler;

    SkillsModifier(EmpHandler handler) {
        this.handler = handler;
    }

    @Override
    protected Collection<Skill> getCurrItems(Emp emp) {
        return emp.getSkills();
    }

    @Override
    protected boolean isSame(Skill currSkill, SkillDto reqSkill) {
        return Objects.equals(currSkill.getSkillTypeId()
            ,reqSkill.getSkillTypeId());
    }

    @Override
    protected void modifyItem(Emp emp, SkillDto reqSkill, Long userId) {
        handler.modifySkill(emp
            , reqSkill.getSkillTypeId()
            , Skill.Level.ofCode(reqSkill.getLevelCode())
            , reqSkill.getDuration()
            , userId);
    }

    @Override
    protected void removeItem(Emp emp, Skill currSkill, Long userId) {
        handler.removeSkill(emp, currSkill.getSkillTypeId());
    }

    @Override
    protected void addItem(Emp emp, SkillDto reqSkill, Long userId) {
        handler.addSkill(emp
            , reqSkill.getSkillTypeId()
            , Skill.Level.ofCode(reqSkill.getLevelCode())
            , reqSkill.getDuration()
            , userId
        );
    }
}