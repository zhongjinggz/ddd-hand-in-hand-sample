package qiyebao.domain.orgmng.emp;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.emp.validator.EmpValidators;

import java.time.LocalDate;

@Component
public class EmpHandler {
    final EmpValidators expect;

    public EmpHandler(EmpValidators expect) {
        this.expect = expect;
    }

    public void modifyIdNum(Emp emp, String newIdNum) {
        if (newIdNum != null) {
            emp.setIdNum(newIdNum);
        }
    }

    public void modifyName(Emp emp, String newName) {
        if (newName != null) {
            emp.setName(newName);
        }
    }

    public void modifyDob(Emp emp, LocalDate newDob) {
        if (newDob != null) {
            expect.dob().ageShouldValid(newDob);
            emp.setDob(newDob);
        }
    }

    public void modifyGender(Emp emp, Gender newGender) {
        if (newGender != null) {
            emp.setGender(newGender);
        }
    }

    public void becomeRegular(Emp emp) {
        emp.becomeRegular();
    }

    public void terminate(Emp emp) {
        emp.terminate();
    }

    public void addSkill(Emp emp
        , Long skillTypeId
        , Skill.Level level
        , Integer duration
        , Long userId
    ) {
        expect.skill().typeShouldValid(emp.getTenantId(), skillTypeId);
        emp.addSkill(skillTypeId, level, duration, userId);
    }

    public void removeSkill(Emp emp, Long skillTypeId, Long userId) {
        emp.removeSkill(skillTypeId, userId);
    }

    public void modifySkill(Emp emp
        , Long skillTypeId
        , Skill.Level level
        , Integer duration
        , Long userId
    ) {
        expect.skill().typeShouldValid(emp.getTenantId(), skillTypeId);
        emp.modifySkill(skillTypeId, level, duration, userId);
    }

    public void addExperience(Emp emp
        , LocalDate startDate
        , LocalDate endDate
        , String company
        , Long userId
    ) {
        emp.addExperience(startDate
            , endDate
            , company
            , userId);
    }

    public void removeExperience(Emp emp
        , LocalDate startDate
        , LocalDate endDate
        , Long userId
    ) {
        emp.removeExperience(startDate, endDate, userId);
    }

    public void modifyExperience(Emp emp
        , LocalDate startDate
        , LocalDate endDate
        , String company
        , Long userId
    ) {
        emp.modifyExperience(startDate
            , endDate
            , company
            , userId);
    }

    public void addPost(Emp emp, String postCode, Long userId) {
        emp.addPost(postCode, userId);
    }

    public void removePost(Emp emp, String postCode, Long userId) {
        emp.removePost(postCode, userId);
    }

    public void setUpdatedInfo(Emp emp, Long userId) {
        emp.setUpdatedInfo(userId);
    }
}
