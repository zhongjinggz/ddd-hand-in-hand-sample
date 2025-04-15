package qiyebao.domain.orgmng.emp.validator;

import org.springframework.stereotype.Component;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.skilltype.SkillType;
import qiyebao.domain.orgmng.skilltype.SkillTypeRepository;

@Component
public class SkillValidator {
    private final SkillTypeRepository skillTypeRepository;

    public SkillValidator(SkillTypeRepository skillTypeRepository) {
        this.skillTypeRepository = skillTypeRepository;
    }

    public void typeShouldValid(Long tenantId, Long skillTypeId) {
        if (!skillTypeRepository.existsByIdAndStatus(tenantId
            , skillTypeId
            , SkillType.Status.EFFECTIVE)
        ) {
            throw new BusinessException(String.format("'%s'不是有效的技能类型代码！", skillTypeId));
        }
    }
}
