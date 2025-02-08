package qiyebao.domain.orgmng.repository;

import qiyebao.domain.orgmng.entity.EmpStatus;

public interface EmpRepository {
    boolean existsByIdAndStatus(Long tenantId, Long id, EmpStatus... statuses);
}
