package qiyebao.domain.orgmng;

import java.util.Arrays;

public enum OrgTypeStatus {
    EFFECTIVE("EF", "有效"),
    TERMINATED("TE", "终止");

    private final String code;
    private final String desc;

    OrgTypeStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;

    }

    public String code() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
