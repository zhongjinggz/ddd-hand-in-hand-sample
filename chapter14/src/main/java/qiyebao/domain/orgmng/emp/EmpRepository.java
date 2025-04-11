package qiyebao.domain.orgmng.emp;

public interface EmpRepository {
    Emp add(Emp emp);

    boolean existsByIdAndStatus(Long tenantId, Long id, Emp.Status... statuses);

    boolean existsByOrgIdAndStatus(Long tenant, Long orgId, Emp.Status... statuses);
}
