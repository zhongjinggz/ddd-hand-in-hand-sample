package qiyebao.common.framework.exception;

public class OptimisticLockException extends BusinessException {
    public OptimisticLockException(String msg) {
        super(msg);
    }
}
