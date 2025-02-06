package qiyebao.domain.tenantmng;

import qiyebao.adapter.driven.persistence.tenantmng.TenantRepository;
import qiyebao.common.framework.exception.BusinessException;

public class TenantValidator {
    private final TenantRepository tenantRepository;

    public TenantValidator( TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    // 租户应当有效
    public void tenantShouldValid(Long tenantId) {
        if (!tenantRepository.existsByIdAndStatus(tenantId, TenantStatus.EFFECTIVE)) {
            throw new BusinessException(
                String.format("id为'%s'的租户不是有效租户！", tenantId)
            );
        }
    }
}
