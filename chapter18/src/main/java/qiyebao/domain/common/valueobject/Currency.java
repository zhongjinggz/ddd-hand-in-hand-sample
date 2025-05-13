package qiyebao.domain.common.valueobject;

import qiyebao.common.framework.domain.CodeEnum;

public enum Currency implements CodeEnum {
    USD("USD", "美元"),
    EUR("EUR", "欧元"),
    CNY("CNY", "人民币"),
    JPY("JPY", "日元"),
    GBP("GBP", "英镑");

    private final String code;
    private final String desc;

    Currency(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Currency ofCode(String code) {
        return CodeEnum.ofCode(Currency.values(), code);
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return code + "(" + desc + ")";
    }
}
