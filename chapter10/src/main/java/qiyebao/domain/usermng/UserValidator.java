package qiyebao.domain.usermng;

import qiyebao.adapter.driven.persistence.usermng.UserRepository;
import qiyebao.common.framework.exception.BusinessException;

public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 用户应当有效
    public void shouldValid(Long tenantId, Long userId) {
        if (!userRepository.existsByIdAndStatus(tenantId, userId
            , UserStatus.EFFECTIVE)) {
            throw new BusinessException(
                String.format("id为'%s'的用户不是有效用户！", userId)
            );
        }
    }
}
