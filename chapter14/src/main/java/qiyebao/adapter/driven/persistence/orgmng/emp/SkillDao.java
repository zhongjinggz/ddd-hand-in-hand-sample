package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.emp.Skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SkillDao {

    private final JdbcHelper jdbc;

    public SkillDao(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "skill");
    }

    Skill insert(Skill skill) {
        Map<String, Object> parms = new HashMap<>();

        parms.put("emp_id", skill.getEmp().getId());
        parms.put("tenant_id", skill.getTenantId());
        parms.put("skill_type_id", skill.getSkillTypeId());
        parms.put("level_code", skill.getLevel().getCode());
        parms.put("duration", skill.getDuration());
        parms.put("created_at", skill.getCreatedAt());
        parms.put("created_by", skill.getCreatedBy());

        jdbc.insert(parms);
        return skill;
    }

    List<TypedMap> selectByEmpId(Long tenantId, Long empId) {
        String sql = """
            select skill_type_id
                 , level_code
                 , duration
                 , created_at
                 , created_by
                 , updated_at
                 , updated_by
            from emp_skill
            where tenant_id = ? 
              emp_id = ?
            """;

        return jdbc.selectMapList(sql, tenantId, empId);
    }
}
