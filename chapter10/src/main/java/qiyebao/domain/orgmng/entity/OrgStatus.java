package qiyebao.domain.orgmng.entity;

import qiyebao.common.framework.domain.CodeEnum;

public enum OrgStatus implements CodeEnum {
    EFFECTIVE("EF", "有效"),
    CANCELLED("CA", "终止");

    private final String code;
    private final String desc;

    OrgStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    public static OrgStatus ofCode(String code) {
        return CodeEnum.ofCode(values(), code);
    }
}