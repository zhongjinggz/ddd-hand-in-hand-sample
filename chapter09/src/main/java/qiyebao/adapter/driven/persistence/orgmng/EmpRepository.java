package qiyebao.adapter.driven.persistence.orgmng;

import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.domain.orgmng.EmpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmpRepository {

    private final JdbcTemplate jdbc;
    private final Selector selector;

    public EmpRepository(JdbcTemplate jdbc
    , Selector selector) {
        this.jdbc = jdbc;
        this.selector = selector;
    }

    public boolean existsByIdAndStatus(Long tenantId, Long id, EmpStatus... statuses) {
        String sql = buildSqlExistsByIdAndStatus(statuses.length);
        Object[] params = buildParamsExistsByIdAndStatus(tenantId, id, statuses);

        return selector.selectExists(sql, params);
    }

    private static Object[] buildParamsExistsByIdAndStatus(Long tenantId, Long id, EmpStatus[] statuses) {
        Object[] params = new Object[2 + statuses.length];
        params[0] = tenantId;
        params[1] = id;
        int j = 2;
        for (EmpStatus status : statuses) {
            params[j++] = status.code();
        }
        return params;
    }

    private static String buildSqlExistsByIdAndStatus(int statusCount) {
        String status_condition = "";
        for (int i = 0; i < statusCount; i++) {
            if (i == 0) {
                status_condition = status_condition + " and (status_code = ?";
            } else {
                status_condition = status_condition + "or status_code = ?";
            }
        }
        status_condition += ")";

        String sql = " select 1 from emp  where tenant_id = ? and id = ?"
                + status_condition
                + " limit 1 ";
        return sql;
    }
}
