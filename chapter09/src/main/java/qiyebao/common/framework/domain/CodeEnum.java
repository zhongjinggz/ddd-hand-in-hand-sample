package qiyebao.common.framework.domain;

public interface CodeEnum {
    String getCode();

    // 根据 code 获取枚举
    static <T extends CodeEnum> T ofCode(T[] values
            , String code) {
        for (T value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("错误的枚举代码 " + code);
    }
}
