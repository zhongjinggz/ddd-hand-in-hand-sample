package qiyebao.domain.orgmng.org.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.emp.EmpRepository;
import qiyebao.domain.orgmng.emp.EmpStatus;
import qiyebao.domain.orgmng.org.OrgStatus;

@Component
public class OrgToBeCancelledValidator {

    private final EmpRepository empRepository;

    @Autowired
    public OrgToBeCancelledValidator(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    // 要被撤销的组织不能有下属员工
    public void shouldNotHasEmp(Long tenantId, Long orgId) {
        if (empRepository.existsByOrgIdAndStatus(tenantId
            , orgId
            , EmpStatus.PROBATION
            , EmpStatus.REGULAR)
        ) {
            throw new BusinessException("该组织中仍然有员工，不能撤销！");
        }
    }

    // 要撤销的组织必须是生效状态
    public void shouldEffective(OrgStatus orgStatus) {
        if (!(orgStatus == OrgStatus.EFFECTIVE)) {
            throw new BusinessException("该组织不是有效状态，不能撤销！");
        }
    }
}
