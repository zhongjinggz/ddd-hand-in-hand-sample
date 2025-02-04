package qiyebao.adapter.driven.persistence.orgmng;

import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.OrgType;
import qiyebao.domain.orgmng.OrgTypeRepository;
import qiyebao.domain.orgmng.OrgTypeStatus;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class OrgTypeRepositoryJdbc implements OrgTypeRepository {
    private final Selector selector;

    public OrgTypeRepositoryJdbc(Selector selector) {
        this.selector = selector;
    }

    @Override
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

        return selector.selectOne(sql
                , this::mapToOrgType
                , tenantId
                , code
                , status.getCode());

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

    @Override
    public boolean existsByCodeAndStatus(Long tenantId, String code, OrgTypeStatus status) {
        final String sql = "select 1"
                + " from org_type "
                + " where tenant_id = ? "
                + " and code = ?"
                + " and status_code = ?"
                + " limit 1";

        return selector.selectExists(sql, tenantId, code, status.getCode());
    }
}
