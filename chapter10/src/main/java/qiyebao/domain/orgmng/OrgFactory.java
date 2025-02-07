package qiyebao.domain.orgmng;

import qiyebao.application.orgmng.OrgDto;

public class OrgFactory {
    private final OrgValidator orgValidator;

    public OrgFactory(OrgValidator orgValidator) {
        this.orgValidator = orgValidator;
    }

    public Org build(OrgDto request, Long userId) {
        orgValidator.validate(request.getTenantId()
            , request.getId()
            , request.getLeaderId()
            , request.getSuperiorId()
            , request.getOrgTypeCode()
            , request.getName()
            , userId);

        return buildOrg(request, userId);
    }

    private Org buildOrg(OrgDto request, Long userId) {
        Org org = new Org();
        org.setOrgTypeCode(request.getOrgTypeCode());
        org.setLeaderId(request.getLeaderId());
        org.setName(request.getName());
        org.setSuperiorId(request.getSuperiorId());
        org.setTenantId(request.getTenantId());
        org.setCreatedBy(userId);
        return org;
    }
}
