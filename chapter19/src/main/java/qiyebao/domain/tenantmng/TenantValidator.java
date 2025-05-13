package qiyebao.domain.tenantmng;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.exception.BusinessException;

@Component
public class TenantValidator {
    private final TenantRepository tenantRepository;

    public TenantValidator( TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    // 租户应当有效
    public void shouldValid(Long tenantId) {
        if (!tenantRepository.existsByIdAndStatus(tenantId, TenantStatus.EFFECTIVE)) {
            throw new BusinessException(
                String.format("id为'%s'的租户不是有效租户！", tenantId)
            );
        }
    }
}
