package qiyebao.adapter.driven.persistence.usermng;

import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.domain.usermng.UserStatus;

@Repository
public class UserRepository {

    private final Selector selector;

    public UserRepository(Selector selector) {
        this.selector = selector;
    }

    public boolean existsByIdAndStatus(long tenantId, long id, UserStatus status) {
        String sql = " select 1 "
                + " from user "
                + " where tenant_id = ? "
                + "   and id = ? "
                + "   and status_code = ? "
                + " limit 1 ";

        return selector.selectExists(sql, tenantId, id, status.getCode());
    }

}
