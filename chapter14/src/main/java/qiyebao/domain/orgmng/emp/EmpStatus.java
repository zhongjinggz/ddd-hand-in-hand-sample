package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.CodeEnum;

public enum EmpStatus implements CodeEnum {
    REGULAR("REG", "正式"),
    PROBATION("PRO", "试用期"),
    TERMINATED("TER", "终止");

    private final String code;
    private final String desc;

    EmpStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EmpStatus ofCode(String code) {
        return CodeEnum.ofCode(EmpStatus.values(), code);
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
