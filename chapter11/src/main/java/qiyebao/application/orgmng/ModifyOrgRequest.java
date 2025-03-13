package qiyebao.application.orgmng;


public class ModifyOrgRequest {
    private Long tenantId;
    private Long leaderId;
    private String name;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
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
        String sb = "OrgDto{" + " tenant=" + tenantId +
                ", leader=" + leaderId +
                ", name='" + name + '\'' +
                '}';
        return sb;
    }
}
