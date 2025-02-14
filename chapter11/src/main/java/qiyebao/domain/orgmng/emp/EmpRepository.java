package qiyebao.domain.orgmng.emp;

public interface EmpRepository {
    boolean existsByIdAndStatus(Long tenantId, Long id, EmpStatus... statuses);
}
