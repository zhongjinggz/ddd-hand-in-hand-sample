package qiyebao.domain.orgmng;

import java.util.Set;

public class OrgType {
    private String code;
    private Set<Org> orgs;
    private String name;
    private String status;

    public Set<Org> getOrgs() {
        return orgs;
    }

    public void setOrgs(Set<Org> orgs) {
        this.orgs = orgs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
