package qiyebao.domain.orgmng.repository;

import qiyebao.domain.orgmng.entity.OrgType;
import qiyebao.domain.orgmng.entity.OrgTypeStatus;

import java.util.Optional;

public interface OrgTypeRepository {
    Optional<OrgType> findByCodeAndStatus(long tenantId, String code, OrgTypeStatus status);

    boolean existsByCodeAndStatus(Long tenantId, String code, OrgTypeStatus status);
}
