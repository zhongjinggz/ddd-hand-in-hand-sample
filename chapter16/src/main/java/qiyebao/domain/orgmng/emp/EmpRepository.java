package qiyebao.domain.orgmng.emp;

import java.util.Optional;

public interface EmpRepository {
    Emp save(Emp emp);

    Optional<Emp> findById(Long tenantId, Long id);

    boolean existsByIdAndStatus(Long tenantId, Long id, Emp.Status... statuses);

    boolean existsByOrgIdAndStatus(Long tenant, Long orgId, Emp.Status... statuses);

}
