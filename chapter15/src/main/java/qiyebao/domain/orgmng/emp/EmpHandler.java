package qiyebao.domain.orgmng.emp;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.domain.orgmng.emp.validator.EmpValidators;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class EmpHandler {
    final EmpValidators expect;

    public EmpHandler(EmpValidators expect) {
        this.expect = expect;
    }

    public void modifyRoot(Emp emp
        , String idNum
        , String name
        , LocalDate dob
        , Gender gender
        , Long userId
    ) {
        modifyIdNum(emp, idNum);
        modifyName(emp, name);
        modifyDob(emp, dob);
        modifyGender(emp, gender);

        emp.setUpdatedInfo(userId);
    }

    private void modifyIdNum(Emp emp, String newIdNum) {
        if (newIdNum != null) {
            expect.idNum().shouldValid(newIdNum);
            emp.setIdNum(newIdNum);
        }
    }

    private void modifyName(Emp emp, String newName) {
        if (newName != null) {
            expect.empName().shouldNotBlank(newName);
            emp.setName(newName);
        }
    }

    private void modifyDob(Emp emp, LocalDate newDob) {
        if (newDob != null) {
            expect.dob().ageShouldValid(newDob);
            emp.setDob(newDob);
        }
    }

    private void modifyGender(Emp emp, Gender newGender) {
        if (newGender != null) {
            emp.setGender(newGender);
        }
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
}
