package qiyebao.adapter.driven.persistence.orgmng;

import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.domain.orgmng.repository.EmpRepository;
import qiyebao.domain.orgmng.entity.EmpStatus;
import org.springframework.stereotype.Repository;

import static org.apache.commons.lang3.ArrayUtils.*;

@Repository
public class EmpRepositoryJdbc implements EmpRepository {

    private final Selector selector;

    public EmpRepositoryJdbc(Selector selector) {
        this.selector = selector;
    }

    @Override
    public boolean existsByIdAndStatus(Long tenantId, Long id, EmpStatus... statuses) {
        String sql = buildSqlExistsByIdAndStatus(statuses.length);
        Object[] params = addAll(toArray(tenantId, id), statuses);

        return selector.selectExists(sql, params);
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
