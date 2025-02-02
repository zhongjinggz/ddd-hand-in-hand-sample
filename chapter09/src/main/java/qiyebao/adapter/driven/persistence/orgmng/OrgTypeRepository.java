package qiyebao.adapter.driven.persistence.orgmng;

import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.OrgType;
import qiyebao.domain.orgmng.OrgTypeStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrgTypeRepository {
    private final JdbcTemplate jdbc;

    public OrgTypeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<OrgType> findByCodeAndStatus(long tenantId, String code, OrgTypeStatus status) {
        final String sql = "select code"
                + ", tenant_id"
                + ", name"
                + ", status_code"
                + ", created_at"
                + ", created_by"
                + ", last_updated_at"
                + ", last_updated_by "
                + "from org_type "
                + "where tenant_id = ? "
                + "and code = ? "
                + "and status_code = ?";

        List<Map<String, Object>> maps
                = jdbc.queryForList(sql
                , tenantId
                , code
                , status.getCode());
        return maps.isEmpty()
                ? Optional.empty()
                : Optional.of(mapToOrgType(maps.getFirst()));
    }

    private OrgType mapToOrgType(Map<String, Object> map) {
        TypedMap typedMap = new TypedMap(map);
        OrgType result = new OrgType();
        result.setCode(typedMap.getString("code"));
        result.setTenantId(typedMap.getLong("tenant_id"));
        result.setName(typedMap.getString("name"));
        result.setStatus(OrgTypeStatus.ofCode(typedMap.getString("status_code")));
        result.setCreatedAt(typedMap.getLocalDateTime("created_at"));
        result.setCreatedBy(typedMap.getLong("created_by"));
        result.setLastUpdatedAt(typedMap.getLocalDateTime("last_updated_at"));
        result.setLastUpdatedBy(typedMap.getLong("last_updated_by"));
        return result;
    }

    public boolean existsByCodeAndStatus(long tenant, String code, OrgTypeStatus status) {
        final String sql = "select 1"
                + " from org_type "
                + " where tenant_id = ? "
                + " and code = ?"
                + " and status_code = ?"
                + " limit 1";

        return !(jdbc.queryForList(sql
                , tenant, code, status.getCode())
        ).isEmpty();
    }
}
