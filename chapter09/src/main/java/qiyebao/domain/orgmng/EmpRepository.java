package qiyebao.domain.orgmng;

public interface EmpRepository {
    boolean existsByIdAndStatus(Long tenantId, Long id, EmpStatus... statuses);
}
