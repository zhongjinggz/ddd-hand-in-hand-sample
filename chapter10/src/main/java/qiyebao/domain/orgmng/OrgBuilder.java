package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrgBuilder {
    private final OrgValidator orgValidator;

    private Long tenantId;
    private Long superiorId;
    private String orgTypeCode;
    private Long leaderId;
    private String name;
    private Long createdBy;

    public OrgBuilder(OrgValidator orgValidator) {
        this.orgValidator = orgValidator;
    }

    public OrgBuilder tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public OrgBuilder superiorId(Long superiorId) {
        this.superiorId = superiorId;
        return this;
    }

    public OrgBuilder orgTypeCode(String orgTypeCode) {
        this.orgTypeCode = orgTypeCode;
        return this;
    }

    public OrgBuilder leaderId(Long leaderId) {
        this.leaderId = leaderId;
        return this;
    }

    public OrgBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OrgBuilder createdBy(Long userId) {
        this.createdBy = userId;
        return this;
    }
    public Org build() {
        orgValidator.validate(tenantId
            , leaderId
            , superiorId
            , orgTypeCode
            , name
            , createdBy);

        Org org = new Org();
        org.setTenantId(tenantId);
        org.setOrgTypeCode(orgTypeCode);
        org.setSuperiorId(superiorId);
        org.setLeaderId(leaderId);
        org.setName(name);
        org.setStatus(OrgStatus.EFFECTIVE);
        org.setCreatedBy(createdBy);
        org.setCreatedAt(LocalDateTime.now());
        return org;
    }

}
