package qiyebao.domain.orgmng.posttype;

import qiyebao.domain.orgmng.orgtype.OrgTypeStatus;

public interface PostTypeRepository {
    boolean existsByCodeAndStatus(Long tenantId, String code, PostType.Status status);
}
