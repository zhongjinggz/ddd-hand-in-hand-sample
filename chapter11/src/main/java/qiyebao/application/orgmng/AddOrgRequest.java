package qiyebao.application.orgmng;

public class AddOrgRequest {
    private Long tenantId;
    private Long superiorId;
    private String orgTypeCode;
    private Long leaderId;
    private String name;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Long superiorId) {
        this.superiorId = superiorId;
    }

    public String getOrgTypeCode() {
        return orgTypeCode;
    }

    public void setOrgTypeCode(String orgTypeCode) {
        this.orgTypeCode = orgTypeCode;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String sb = "OrgDto{ tenant=" + tenantId +
                ", superior=" + superiorId +
                ", orgType='" + orgTypeCode + '\'' +
                ", leader=" + leaderId +
                ", name='" + name + '\'' +
                '}';
        return sb;
    }
}
