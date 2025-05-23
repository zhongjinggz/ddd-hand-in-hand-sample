package qiyebao.adapter.driven.persistence.tenantmng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.Selector;
import qiyebao.domain.tenantmng.TenantRepository;
import qiyebao.domain.tenantmng.TenantStatus;

@Repository
public class TenantRepositoryJdbc implements TenantRepository {
    private final Selector selector;

    @Autowired
    public TenantRepositoryJdbc(Selector selector) {
        this.selector = selector;
    }

    @Override
    public boolean existsByIdAndStatus(long tenantId, TenantStatus status) {
        final String sql = " select 1 "
                + " from tenant "
                + " where id = ? "
                + "   and status = ?";
        return selector.selectExists(sql, tenantId, status.getCode());
    }

}
