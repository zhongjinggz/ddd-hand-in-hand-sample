package qiyebao.domain.tenantmng;

public interface TenantRepository {
    boolean existsByIdAndStatus(long tenantId, TenantStatus status);
}
