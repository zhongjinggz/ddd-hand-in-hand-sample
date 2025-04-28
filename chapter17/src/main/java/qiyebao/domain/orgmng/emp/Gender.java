package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.CodeEnum;

public enum Gender implements CodeEnum {
    MALE("M", "男"),
    FEMALE("F", "女");

    private final String code;
    private final String desc;

    public static Gender ofCode(String code) {
        return CodeEnum.ofCode(values(), code);
    }

    Gender(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
