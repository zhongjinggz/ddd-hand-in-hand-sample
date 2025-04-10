package qiyebao.domain.orgmng.skilltype;

import qiyebao.domain.orgmng.posttype.PostType;

public interface SkillTypeRepository {
    boolean existsByIdAndStatus(Long tenantId
        , Long id
        , SkillType.Status status);
}
