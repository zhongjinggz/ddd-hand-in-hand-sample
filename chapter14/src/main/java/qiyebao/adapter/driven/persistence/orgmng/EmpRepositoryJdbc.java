package qiyebao.adapter.driven.persistence.orgmng;

import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.EmpRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

import static org.apache.commons.lang3.ArrayUtils.*;

@Repository
public class EmpRepositoryJdbc implements EmpRepository {

    private final Selector selector;

    public EmpRepositoryJdbc(Selector selector) {
        this.selector = selector;
    }

    @Override
    public boolean existsByIdAndStatus(Long tenantId, Long id, Emp.Status... statuses) {
        String sql = " select 1 from emp  "
            + "where tenant_id = ? and id = ?"
            + statusCondition(statuses.length)
            + " limit 1 ";

        Object[] params = addAll( toArray(tenantId, id)
            , Arrays.stream(statuses).map(Emp.Status::getCode).toArray()
        );

        return selector.selectExists(sql, params);
    }

    @Override
    public boolean existsByOrgIdAndStatus(Long tenantId, Long orgId, Emp.Status... statuses) {
        String sql = " select 1 from emp  "
            + "where tenant_id = ? and orgId = ?"
            + statusCondition(statuses.length)
            + " limit 1 ";

        Object[] params = addAll( toArray(tenantId, orgId)
            , Arrays.stream(statuses).map(Emp.Status::getCode).toArray()
        );

        return selector.selectExists(sql, params);
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
