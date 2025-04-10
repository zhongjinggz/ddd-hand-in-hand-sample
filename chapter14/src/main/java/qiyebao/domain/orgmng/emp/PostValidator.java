package qiyebao.domain.orgmng.emp;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.posttype.PostType;
import qiyebao.domain.orgmng.posttype.PostTypeRepository;

@Component
public class PostValidator {
    PostTypeRepository postTypeRepository;

    public void typeShouldValid(Long tenantId, String postTypeCode) {
        if (!postTypeRepository.existsByCodeAndStatus(tenantId
            , postTypeCode
            , PostType.Status.EFFECTIVE)
        ) {
            throw new BusinessException(String.format("'%s'不是有效的岗位类型代码！", postTypeCode));
        }
    }
}
