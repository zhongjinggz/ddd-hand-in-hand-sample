package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.WorkExperience;

import java.util.HashMap;
import java.util.List;
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

    public List<TypedMap> selectByEmpId(Long tenantId, Long id) {
        var sql = """
            select start_date
                 , end_date
                 , company
                 , created_at
                 , created_by
                 , updated_at
                 , updated_by
            from work_experience
            where tenant_id = ? 
              and emp_id = ?
            """;

        return jdbc.selectMapList(sql, tenantId, id);
    }

    public void save(WorkExperience experience) {
    }

    void deleteByEmpId(Emp emp, Long empId) {
        jdbc.delete("""
                delete from work_experience 
                where tenant_id = ? and emp_id = ?
                """
            , emp.getTenantId()
            , empId);
    }
}
