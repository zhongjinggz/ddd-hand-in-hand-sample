package qiyebao.domain.orgmng;

import qiyebao.common.framework.exception.BusinessException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class OrgNameValidator {
    private final OrgRepository orgRepository;

    public OrgNameValidator(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    // 组织名称不能为空
    public void shouldNotBlank(String name) {
        if (isBlank(name)) {
            throw new BusinessException("组织没有名称！");
        }
    }

    // 同一上级下的(直属）组织的名称不能重复
    public void underSameSuperiorShouldNotDuplicated(
        Long tenantId
        , Long superiorId
        , String name
    ) {
        if (orgRepository.existsBySuperiorAndName(tenantId
            , superiorId
            , name)) {
            throw new BusinessException(
                String.format("同一上级下已经有名为'%s'的组织存在！", name)
            );
        }
    }
}
