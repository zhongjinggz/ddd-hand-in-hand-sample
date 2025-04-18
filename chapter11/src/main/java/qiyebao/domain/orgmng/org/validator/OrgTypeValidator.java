package qiyebao.domain.orgmng.org.validator;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.orgtype.OrgTypeStatus;
import qiyebao.domain.orgmng.orgtype.OrgTypeRepository;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class OrgTypeValidator {
    private final OrgTypeRepository orgTypeRepository;

    public OrgTypeValidator(OrgTypeRepository orgTypeRepository) {
        this.orgTypeRepository = orgTypeRepository;
    }
    // 组织类别不能为空
    public void shouldNotBlank(String orgTypeCode) {
        if (isBlank(orgTypeCode)) {
            throw new BusinessException("组织类别不能为空！");
        }
    }

    // 组织类别应当有效
    public void shouldValid(Long tenantId, String orgTypeCode) {
        if (!orgTypeRepository.existsByCodeAndStatus(tenantId
            , orgTypeCode
            , OrgTypeStatus.EFFECTIVE)
        ) {
            throw new BusinessException(
                String.format("'%s'不是有效的组织类别代码！", orgTypeCode));
        }
    }

    // 组织类型不应是企业（企业是在创建租户的时候添加的，因此不能单独添加企业）
    public void shouldNotEntp(String orgTypeCode) {
        if ("ENTP".equals(orgTypeCode)) {
            throw new BusinessException(
                "企业是在创建租户的时候添加的，因此不能单独添加企业!");
        }
    }
}
