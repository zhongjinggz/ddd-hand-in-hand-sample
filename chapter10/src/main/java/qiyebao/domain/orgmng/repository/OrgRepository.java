package qiyebao.domain.orgmng.repository;  // [1] 注意，仓库接口在领域层

import qiyebao.domain.orgmng.entity.OrgStatus;
import qiyebao.domain.orgmng.entity.Org;

import java.util.Optional;

public interface OrgRepository {
    Optional<Org> findByIdAndStatus(Long tenantId, Long id, OrgStatus status);

    Optional<Org> findById(long tenantId, long id);

    Org save(Org org);

    boolean existsBySuperiorAndName(Long tenantId, Long superiorId, String name);

    int update(Org org);
}
