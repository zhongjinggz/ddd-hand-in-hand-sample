package qiyebao.adapter.driven.persistence.orgmng.skilltype;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.domain.orgmng.skilltype.SkillType;
import qiyebao.domain.orgmng.skilltype.SkillTypeRepository;

@Repository
public class SkillTypeRepositoryJdbc implements SkillTypeRepository {
    private final JdbcHelper jdbc;

    public SkillTypeRepositoryJdbc(JdbcTemplate jdbcTemplate){
        this.jdbc = new JdbcHelper(jdbcTemplate, "skill_type", "id" );
     }

    @Override
    public boolean existsByIdAndStatus(Long tenantId
        , Long id
        , SkillType.Status status
    ) {
        final String sql = "select 1"
                + " from skill_type "
                + " where tenant_id = ? "
                + " and id = ?"
                + " and status_code = ?"
                + " limit 1";

        return jdbc.selectExists(sql, tenantId, id, status.getCode());
    }
}
