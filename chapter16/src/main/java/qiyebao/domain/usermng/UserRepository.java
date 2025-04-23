package qiyebao.domain.usermng;

public interface UserRepository {
    boolean existsByIdAndStatus(Long tenantId, Long userId, UserStatus userStatus);
}
