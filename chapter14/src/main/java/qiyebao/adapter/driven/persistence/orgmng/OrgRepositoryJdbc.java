package qiyebao.adapter.driven.persistence.orgmng; // [1] 注意，仓库的实现在适配器层

import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.common.utils.SqlUtils;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.org.Org;
import qiyebao.domain.orgmng.org.OrgRepository;
import qiyebao.domain.orgmng.org.OrgStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static qiyebao.common.utils.ReflectUtils.forceSet;

@Repository
public class OrgRepositoryJdbc implements OrgRepository {
    private final JdbcHelper jdbc;

    public OrgRepositoryJdbc(JdbcTemplate jdbcTemplate
        , JdbcHelper jdbc) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "org", "id");
    }

    private static String fields = " id"
        + ", tenant_id"
        + ", superior_id"
        + ", org_type_code"
        + ", leader_id"
        + ", name"
        + ", status_code"
        + ", created_at"
        + ", created_by"
        + ", last_updated_at"
        + ", last_updated_by ";

    @Override
    public Optional<Org> findByIdAndStatus(Long tenantId, Long id, OrgStatus status) {
        final String sql = " select " + fields
            + " from org "
            + " where tenant_id = ?  "
            + "   and id = ? "
            + "   and status_code = ? ";

        return jdbc.selectOne(sql
            , this::mapToOrg
            , tenantId
            , id
            , status.getCode());
    }

    @Override
    public Optional<Org> findById(long tenantId, long id) {
        final String sql = " select " + fields
            + " from org "
            + " where tenant_id = ? "
            + "  and id = ?";

        return jdbc.selectOne(sql
            , this::mapToOrg
            , tenantId
            , id);
    }

    private Org mapToOrg(ResultSet rs, int rowNum) throws SQLException {
        Org result = new Org(
                rs.getLong("id")
                , rs.getLong("tenant_id")
                , rs.getString("org_type_code")
                , rs.getTimestamp("created_at").toLocalDateTime()
                , rs.getLong("created_by")
        );
        result.setSuperiorId(rs.getLong("superior_id"));
        result.setLeaderId(rs.getLong("leader_id"));
        result.setName(rs.getString("name"));
        result.setStatus(OrgStatus.ofCode(rs.getString("status_code")));
        result.setUpdatedAt(SqlUtils.toLocalDateTime(rs, "updated_at"));
        result.setUpdatedBy(rs.getLong("updated_by"));
        return result;

    }

    @Override
    public boolean existsByIdAndStatus(Long tenantId, Long id, OrgStatus status) {
        String sql = """ 
            select 1 from org
            where tenant_id = ?  and id = ? and status_code = ?"
            limit 1 
            """;
        return jdbc.selectExists(sql, tenantId, id, status.getCode());
    }
    @Override
    public Org add(Org org) {
        Map<String, Object> params = new HashMap<>(8);

        params.put("tenant_id", org.getTenantId());
        params.put("superior_id", org.getSuperiorId());
        params.put("org_type_code", org.getOrgTypeCode());
        params.put("leader_id", org.getLeaderId());
        params.put("name", org.getName());
        params.put("status_code", org.getStatus().getCode());
        params.put("created_at", org.getCreatedAt());
        params.put("created_by", org.getCreatedBy());

        Number createdId = jdbc.insertAndReturnKey(params);

        forceSet(org, "id", createdId.longValue());

        return org;
    }

    @Override
    public boolean existsBySuperiorAndName(Long tenantId, Long superiorId, String name) {
        final String sql = " select 1 "
            + " from org "
            + " where tenant_id = ?  "
            + "   and superior_id = ? "
            + "   and name = ?"
            + " limit 1 ";

        return jdbc.selectExists(sql
            , tenantId
            , superiorId
            , name);
    }

    @Override
    public int modify(Org org) {
        String sql = "update org "
            + " set superior_id = ? "
            + ", org_type_code =? "
            + ", leader_id = ?"
            + ", name = ?"
            + ", status_code =?"
            + ", last_updated_at = ?"
            + ", last_updated_by = ? "
            + " where tenant_id = ? and id = ? ";

        return jdbc.update(sql
            , org.getSuperiorId()
            , org.getOrgTypeCode()
            , org.getLeaderId()
            , org.getName()
            , org.getStatus().getCode()
            , org.getUpdatedAt()
            , org.getUpdatedBy()
            , org.getTenantId()
            , org.getId()
        );
    }

}
