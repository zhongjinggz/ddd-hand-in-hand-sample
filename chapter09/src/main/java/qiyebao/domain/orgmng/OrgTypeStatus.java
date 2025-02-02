package qiyebao.domain.orgmng;

import qiyebao.common.framework.domain.CodeEnum;

public enum OrgTypeStatus implements CodeEnum {
    EFFECTIVE("EF", "有效"),
    TERMINATED("TE", "终止");

    private final String code;
    private final String desc;

    OrgTypeStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public static OrgTypeStatus ofCode(String code) {
        return CodeEnum.ofCode(values(), code);
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
