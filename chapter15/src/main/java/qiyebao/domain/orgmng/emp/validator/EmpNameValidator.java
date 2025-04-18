package qiyebao.domain.orgmng.emp.validator;

import qiyebao.common.framework.exception.BusinessException;

public class EmpNameValidator {
    public void shouldNotBlank(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("姓名不能为空！");
        }
    }
}
