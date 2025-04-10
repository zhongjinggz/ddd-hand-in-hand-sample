package qiyebao.domain.orgmng.org;  // [1] 注意，仓库接口在领域层

import java.util.Optional;

public interface OrgRepository {
    Org add(Org org);

    int modify(Org org);

    Optional<Org> findByIdAndStatus(Long tenantId, Long id, OrgStatus status);

    Optional<Org> findById(long tenantId, long id);

    boolean existsBySuperiorAndName(Long tenantId, Long superiorId, String name);

    boolean existsByIdAndStatus(Long tenantId, Long orgId, OrgStatus orgStatus);
}
