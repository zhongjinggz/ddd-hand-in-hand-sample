package qiyebao.domain.orgmng.emp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qiyebao.common.framework.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmpTest {

    private Emp emp;

    @BeforeEach
    public void setUp() {
        emp = new Emp(1L, Emp.Status.REGULAR, 100L);
    }

    @Test
    public void addSkill_SkillTypeNotDuplicated_ShouldAddSkill() {
        emp.addSkill(1L, Skill.Level.BEGINNER, 12, 100L);

        List<Skill> skills = emp.getSkills();
        assertEquals(1, skills.size());

        Skill skill = skills.get(0);
        assertEquals(1L, skill.getSkillTypeId());
        assertEquals(Skill.Level.BEGINNER, skill.getLevel());
        assertEquals(12, skill.getDuration());
        assertNotNull(skill.getCreatedAt());
        assertEquals(100L, skill.getCreatedBy());
    }

    @Test
    public void addSkill_SkillTypeDuplicated_ShouldThrowBusinessException() {
        emp.addSkill(1L, Skill.Level.BEGINNER, 12, 100L);

        assertThrows(BusinessException.class, () -> {
            emp.addSkill(1L, Skill.Level.ADVANCED, 24, 100L);
        });
    }

    @Test
    public void addExperience_NoOverlap_ShouldAddExperience() {
        emp.addExperience(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31), "Company A", 100L);

        List<WorkExperience> experiences = emp.getExperiences();
        assertEquals(1, experiences.size());

        WorkExperience experience = experiences.get(0);
        assertEquals("Company A", experience.getCompany());
        assertEquals(LocalDate.of(2020, 1, 1), experience.getStartDate());
        assertEquals(LocalDate.of(2020, 12, 31), experience.getEndDate());
        assertNotNull(experience.getCreatedAt());
        assertEquals(100L, experience.getCreatedBy());
    }

    @Test
    public void addExperience_Overlap_ShouldThrowBusinessException() {
        emp.addExperience(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31), "Company A", 100L);

        assertThrows(BusinessException.class, () -> {
            emp.addExperience(LocalDate.of(2020, 6, 1), LocalDate.of(2021, 6, 1), "Company B", 100L);
        });
    }


}
