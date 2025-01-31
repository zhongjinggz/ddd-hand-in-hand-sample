package qiyebao.domain.orgmng;

import java.util.Arrays;

public enum OrgStatus {
    EFFECTIVE("EF", "有效"),
    CANCELLED("CA", "终止");

    private final String code;
    private final String desc;

    OrgStatus(String code, String desc) {
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
