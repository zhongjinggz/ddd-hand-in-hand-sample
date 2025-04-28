package qiyebao.domain.orgmng.empnumcounter;

import java.util.Optional;

public interface EmpNumCounterRepository {
    EmpNumCounter save(EmpNumCounter empNumCounter);

    Optional<EmpNumCounter> findByYear(Long tenantId, Integer yearNum);

    Integer nextNumByYear(Long tenantId, Integer yearNum);
}
