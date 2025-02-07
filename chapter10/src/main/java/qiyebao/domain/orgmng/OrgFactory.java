package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;

@Component
public class OrgFactory {
    private final OrgValidator orgValidator;

    public OrgFactory(OrgValidator orgValidator) {
        this.orgValidator = orgValidator;
    }

    public Org build(Long tenantId
        , String orgTypeCode
        , Long superiorId
        , Long leaderId
        , String name
        , Long userId
    ) {
        orgValidator.validate(tenantId
            , leaderId
            , superiorId
            , orgTypeCode
            , name
            , userId);

        Org org = new Org();
        org.setOrgTypeCode(orgTypeCode);
        org.setLeaderId(leaderId);
        org.setName(name);
        org.setSuperiorId(superiorId);
        org.setTenantId(tenantId);
        org.setCreatedBy(userId);
        return org;
    }

}
