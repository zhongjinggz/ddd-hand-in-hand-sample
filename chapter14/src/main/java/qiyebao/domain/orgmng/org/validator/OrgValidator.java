package qiyebao.domain.orgmng.org.validator;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.org.OrgRepository;
import qiyebao.domain.orgmng.org.OrgStatus;

@Component
public class OrgValidator {
    private final OrgRepository orgRepository;

    public OrgValidator(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    // 上级组织应当有效
    public void shouldValid(Long tenantId, Long orgId) {
        if (!orgRepository.existsByIdAndStatus(tenantId, orgId, OrgStatus.EFFECTIVE)){
            throw new BusinessException("id为'" + orgId + "'的组织不是有效组织！");
        }
    }

}
