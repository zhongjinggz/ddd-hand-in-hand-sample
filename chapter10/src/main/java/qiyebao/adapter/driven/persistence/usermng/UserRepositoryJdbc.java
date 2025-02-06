package qiyebao.adapter.driven.persistence.usermng;

import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.domain.usermng.UserRepository;
import qiyebao.domain.usermng.UserStatus;

@Repository
public class UserRepositoryJdbc implements UserRepository {

    private final Selector selector;

    public UserRepositoryJdbc(Selector selector) {
        this.selector = selector;
    }

    @Override
    public boolean existsByIdAndStatus(Long tenantId, Long id, UserStatus status) {
        String sql = " select 1 "
                + " from user "
                + " where tenant_id = ? "
                + "   and id = ? "
                + "   and status_code = ? "
                + " limit 1 ";

        return selector.selectExists(sql, tenantId, id, status.getCode());
    }

}
