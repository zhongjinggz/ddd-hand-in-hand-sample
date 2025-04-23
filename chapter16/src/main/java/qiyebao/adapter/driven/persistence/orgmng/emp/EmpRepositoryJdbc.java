package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.common.framework.domain.AuditInfo;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Emp save(Emp emp) {
        switch (emp.getPersistentStatus()) {
            case NEW:
                insertEmp(emp);
                saveSubEntries(emp);
                break;
            case UPDATED:
                updateEmp(emp);
                saveSubEntries(emp);
                break;
            case DELETED:
                removeSubEntries(emp);
                deleteEmp(emp);
                break;
        }
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

    private void deleteEmp(Emp emp) {
        jdbc.delete("""
                delete from emp 
                where tenant_id = ? and id = ?
                """
            , emp.getTenantId()
            , emp.getId());
    }

    private void updateEmp(Emp emp) {
        String sql = """
             update emp 
             set org_id = ?
               , emp_num = ?
               , id_num =? 
               , name = ?
               , gender_code =?
               , dob = ?
               , status_code =?
               , updated_at =?
               , updated_by =? 
              where tenant_id = ? and id = ?
            """;
        jdbc.update(sql
            , emp.getOrgId()
            , emp.getEmpNum()
            , emp.getIdNum()
            , emp.getName()
            , emp.getGender().getCode()
            , emp.getDob()
            , emp.getStatus().getCode()
            , emp.getUpdatedAt()
            , emp.getUpdatedBy()
            , emp.getTenantId()
            , emp.getId());
    }

    private void saveSubEntries(Emp emp) {
        emp.getSkills().forEach(skillDao::save);
        emp.getExperiences().forEach(workExperienceDao::save);
        emp.getPosts().forEach(postDao::save);
    }

    private void removeSubEntries(Emp emp) {
        skillDao.deleteByEmpId(emp, emp.getId());
        workExperienceDao.deleteByEmpId(emp, emp.getId());
        postDao.deleteByEmpId(emp, emp.getId());
    }

    @Override
    public Optional<Emp> findById(Long tenantId, Long id) {
        Emp.Loader loader = loadEmp(tenantId, id);

        if (loader == null) {
            return Optional.empty();
        }

        loadSkills(loader, tenantId, id);
        loadExperiences(loader, tenantId, id);
        loadPosts(loader, tenantId, id);
        return Optional.of(loader.load());
    }

    private Emp.Loader loadEmp(Long tenantId, Long id) {
        List<TypedMap> empMapList = selectEmpById(tenantId, id);
        if (empMapList.isEmpty()) {
            return null;
        } else {
            TypedMap empMap = empMapList.get(0);
            var loader = Emp.loader()
                .tenantId(empMap.getLong("tenant_id"))
                .id(empMap.getLong("id"))
                .orgId(empMap.getLong("org_id"))
                .empNum(empMap.getString("emp_num"))
                .idNum(empMap.getString("id_num"))
                .name(empMap.getString("name"))
                .genderCode(empMap.getString("gender_code"))
                .dob(empMap.getLocalDate("dob"))
                .statusCode(empMap.getString("status_code"))
                .createdAt(empMap.getLocalDateTime("created_at"))
                .createdBy(empMap.getLong("created_by"))
                .updatedAt(empMap.getLocalDateTime("updated_at"))
                .updatedBy(empMap.getLong("updated_by"));
            return loader;
        }
    }

    private List<TypedMap> selectEmpById(Long tenantId, Long id) {
        String sql = """
            select org_id
                 , emp_num
                 , id_num
                 , name
                 , gender_code
                 , dob
                 , status_code
                 , created_at
                 , created_by
                 , updated_at
                 , updated_by
            from emp
            where tenant_id = ? and id = ? 
            """;

        return jdbc.selectMapList(sql, tenantId, id);
    }

    private void loadSkills(Emp.Loader loader, Long tenantId, Long id) {
        var skillMapList = skillDao.selectByEmpId(tenantId, id);

        for (var skillMap : skillMapList) {
            loader.loadSkill(skillMap.getLong("skill_type_id")
                , skillMap.getString("level_code")
                , skillMap.getInteger("duration")
                , new AuditInfo(skillMap.getLocalDateTime("created_at")
                    , skillMap.getLong("created_by")
                    , skillMap.getLocalDateTime("updated_at")
                    , skillMap.getLong("updated_by")));
        }
    }

    private void loadExperiences(Emp.Loader loader, Long tenantId, Long id) {
        List<TypedMap> expMapList = workExperienceDao.selectByEmpId(tenantId, id);
        for (var expMap : expMapList) {
            loader.loadExperience(expMap.getLocalDate("start_date")
                , expMap.getLocalDate("end_date")
                , expMap.getString("company")
                , new AuditInfo(expMap.getLocalDateTime("created_at")
                    , expMap.getLong("created_by")
                    , expMap.getLocalDateTime("updated_at")
                    , expMap.getLong("updated_by")));
        }
    }

    private void loadPosts(Emp.Loader loader, Long tenantId, Long id) {
        List<TypedMap> postMapList = postDao.selectByEmpId(tenantId, id);
        for (var postMap : postMapList) {
            loader.loadPost(postMap.getString("post_type_code")
                , new AuditInfo(postMap.getLocalDateTime("created_at")
                    , postMap.getLong("created_by")
                    , postMap.getLocalDateTime("updated_at")
                    , postMap.getLong("updated_by")));
        }
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
