package qiyebao.domain.orgmng.orgtype;

import java.util.Optional;

public interface OrgTypeRepository {
    Optional<OrgType> findByCodeAndStatus(long tenantId, String code, OrgTypeStatus status);

    boolean existsByCodeAndStatus(Long tenantId, String code, OrgTypeStatus status);
}
