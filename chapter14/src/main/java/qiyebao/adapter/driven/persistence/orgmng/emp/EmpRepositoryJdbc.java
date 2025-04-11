package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Map;

import static org.apache.commons.lang3.ArrayUtils.*;
import static qiyebao.common.utils.ReflectUtils.forceSet;

@Repository
public class EmpRepositoryJdbc implements EmpRepository {

    private final JdbcHelper jdbc;
    private final SkillDao skillDao;
    private final WorkExperienceDao workExperienceDao;
    private final PostDao postDao;

    public EmpRepositoryJdbc(JdbcTemplate jdbcTemplate
        , SkillDao skillDao
        , WorkExperienceDao workExperienceDao
        , PostDao postDao
    ) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "emp", "id");
        this.skillDao = skillDao;
        this.workExperienceDao = workExperienceDao;
        this.postDao = postDao;
    }

    @Override
    public Emp add(Emp emp) {
        insertEmp(emp);

        emp.getSkills().forEach(s -> skillDao.insert(s));
        emp.getExperiences().forEach(e -> workExperienceDao.insert(e));
        emp.getPosts().forEach(p -> postDao.insert(p));

        return emp;
    }

    private void insertEmp(Emp emp) {
        Map<String, Object> parms = Map.of(
            "tenant_id", emp.getTenantId()
            , "org_id", emp.getOrgId()
            , "emp_num", emp.getEmpNum()
            , "id_num", emp.getIdNum()
            , "name", emp.getName()
            , "gender", emp.getGender().getCode()
            , "dob", emp.getDob()
            , "status", emp.getStatus().getCode()
            , "created_at", emp.getCreatedAt()
            , "created_by", emp.getCreatedBy()
        );

        Number createdId = jdbc.insertAndReturnKey(parms);

        forceSet(emp, "id", createdId.longValue());
    }

    @Override
    public boolean existsByIdAndStatus(Long tenantId, Long id, Emp.Status... statuses) {
        String sql = " select 1 from emp  "
            + "where tenant_id = ? and id = ?"
            + statusCondition(statuses.length)
            + " limit 1 ";

        Object[] params = addAll(toArray(tenantId, id)
            , Arrays.stream(statuses).map(Emp.Status::getCode).toArray()
        );

        return jdbc.selectExists(sql, params);
    }

    @Override
    public boolean existsByOrgIdAndStatus(Long tenantId, Long orgId, Emp.Status... statuses) {
        String sql = " select 1 from emp  "
            + "where tenant_id = ? and orgId = ?"
            + statusCondition(statuses.length)
            + " limit 1 ";

        Object[] params = addAll(toArray(tenantId, orgId)
            , Arrays.stream(statuses).map(Emp.Status::getCode).toArray()
        );

        return jdbc.selectExists(sql, params);
    }

    private static String statusCondition(int statusCount) {
        StringBuilder statusCondition = new StringBuilder();

        for (int i = 0; i < statusCount; i++) {
            if (i == 0) {
                statusCondition.append(" and (status_code = ?");
            } else {
                statusCondition.append("or status_code = ?");
            }
        }
        statusCondition.append(")");
        return statusCondition.toString();
    }
}
