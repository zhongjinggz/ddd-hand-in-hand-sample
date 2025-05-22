package qiyebao.domain.orgmng.empnumcounter;

public class EmpNumCounter {
    final private Long tenantId;
    final private Integer yearNum;
    final private Integer maxEmpNum;

    public EmpNumCounter(long tenantId, int yearNum, int maxEmpNum) {
        this.tenantId = tenantId;
        this.yearNum = yearNum;
        this.maxEmpNum = maxEmpNum;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Integer getYearNum() {
        return yearNum;
    }

    public Integer getMaxEmpNum() {
        return maxEmpNum;
    }

}
