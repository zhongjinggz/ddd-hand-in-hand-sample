package qiyebao.adapter.driven.persistence.tenantmng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.existsSelector;
import qiyebao.domain.tenantmng.TenantStatus;

import static org.apache.commons.lang3.ArrayUtils.*;

@Repository
public class TenantRepository implements existsSelector {
    JdbcTemplate jdbc;

    @Autowired
    public TenantRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    public boolean existsByIdAndStatus(long tenantId, TenantStatus status) {
        final String sql = " select 1 "
                + " from tenant "
                + " where id = ? "
                + "   and status = ?";
        return selectExists(sql, toArray(tenantId, status.getCode()));
    }

}
