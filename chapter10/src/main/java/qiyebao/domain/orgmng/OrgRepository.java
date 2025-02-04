package qiyebao.domain.orgmng;  // [1] 注意，仓库接口在领域层

import java.util.Optional;

public interface OrgRepository {
    Optional<Org> findByIdAndStatus(Long tenantId, Long id, OrgStatus status);

    Optional<Org> findById(long tenantId, long id);

    Org save(Org org);

    boolean existsBySuperiorAndName(Long tenantId, Long superiorId, String name);

    int update(Org org);
}
