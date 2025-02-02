package qiyebao.adapter.driven.persistence.usermng;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.existsSelector;
import qiyebao.domain.usermng.UserStatus;

import static org.apache.commons.lang3.ArrayUtils.*;

@Repository
public class UserRepository implements existsSelector {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public JdbcTemplate getJdbc() {
        return jdbc;
    }
    public boolean existsByIdAndStatus(long tenantId, long id, UserStatus status) {
        String sql = " select 1 "
                + " from user "
                + " where tenant_id = ? "
                + "   and id = ? "
                + "   and status_code = ? "
                + " limit 1 ";

        return selectExists(sql, toArray(tenantId, id, status.getCode()));
    }

}
