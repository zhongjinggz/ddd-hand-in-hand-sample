package qiyebao.domain.orgmng;

import qiyebao.common.framework.exception.BusinessException;

public class OrgLeaderValidator {
    private final EmpRepository empRepository;
    public OrgLeaderValidator(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    // 组织负责人可以空缺，如果有的话，的必须是一个在职员工（含试用期）
    public void shouldValid(Long tenantId, Long leaderId) {
        if (leaderId != null
            && !empRepository.existsByIdAndStatus(tenantId
            , leaderId
            , EmpStatus.REGULAR
            , EmpStatus.PROBATION)) {
            throw new BusinessException(
                String.format("组织负责人(id='%s')不是在职员工！", leaderId)
            );
        }
    }
}
