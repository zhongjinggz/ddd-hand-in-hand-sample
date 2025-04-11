package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.domain.orgmng.emp.WorkExperience;

import java.util.HashMap;
import java.util.Map;

@Component
public class WorkExperienceDao {

    private final JdbcHelper jdbc;

    public WorkExperienceDao(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "work_experience");
    }

    WorkExperience insert(WorkExperience experience) {
        Map<String, Object> parms = new HashMap<>();

        parms.put("emp_id", experience.getEmp().getId());
        parms.put("tenant_id", experience.getTenantId());
        parms.put("start_date", experience.getStartDate());
        parms.put("end_date", experience.getEndDate());
        parms.put("company", experience.getCompany());
        parms.put("created_at", experience.getCreatedAt());
        parms.put("created_by", experience.getCreatedBy());

        jdbc.insertAndReturnKey(parms);
        return experience;
    }

}
