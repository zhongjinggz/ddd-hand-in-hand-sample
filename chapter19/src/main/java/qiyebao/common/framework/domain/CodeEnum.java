package qiyebao.common.framework.domain;

public interface CodeEnum {
    String getCode();

    // 根据 code 获取枚举
    static <T extends CodeEnum> T ofCode(T[] values
        , String code
    ) {
        T result = codeToEnum(values, code);
        if (result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("错误的枚举代码 " + code);
        }
    }

    static <T extends CodeEnum> boolean isValidCode(T[] values
        , String code) {
        return codeToEnum(values, code) != null;
    }

    private static <T extends CodeEnum> T codeToEnum(T[] values
        , String code) {
        for (T value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
