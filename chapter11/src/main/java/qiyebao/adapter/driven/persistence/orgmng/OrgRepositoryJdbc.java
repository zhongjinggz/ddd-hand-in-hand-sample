package qiyebao.adapter.driven.persistence.orgmng; // [1] 注意，仓库的实现在适配器层

import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.org.Org;
import qiyebao.domain.orgmng.org.OrgRepository;
import qiyebao.domain.orgmng.org.OrgStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static qiyebao.common.utils.ReflectUtils.forceSet;

@Repository
public class OrgRepositoryJdbc implements OrgRepository {
    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insertOrg;
    private final Selector selector;

    public OrgRepositoryJdbc(JdbcTemplate jdbc
        , Selector selector) {
        this.jdbc = jdbc;
        this.insertOrg = new SimpleJdbcInsert(jdbc)
            .withTableName("org")
            .usingGeneratedKeyColumns("id");
        this.selector = selector;
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

        return selector.selectOne(sql
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

        return selector.selectOne(sql
            , this::mapToOrg
            , tenantId
            , id);
    }

    private Org mapToOrg(Map<String, Object> map) {
        TypedMap typedMap = new TypedMap(map);

        Org result = new Org(typedMap.getLong("id")
            , typedMap.getLong("tenant_id")
            , typedMap.getString("org_type_code")
            , typedMap.getLocalDateTime("created_at")
            , typedMap.getLong("created_by")
        );
        result.setSuperiorId(typedMap.getLong("superior_id"));
        result.setLeaderId(typedMap.getLong("leader_id"));
        result.setName(typedMap.getString("name"));
        result.setStatus(OrgStatus.ofCode(typedMap.getString("status_code")));
        result.setUpdatedAt(typedMap.getLocalDateTime("last_updated_at"));
        result.setUpdatedBy(typedMap.getLong("last_updated_by"));

        return result;
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

        Number createdId = insertOrg.executeAndReturnKey(params);

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

        return selector.selectExists(sql
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

        return this.jdbc.update(sql
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
