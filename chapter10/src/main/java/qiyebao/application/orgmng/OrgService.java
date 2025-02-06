package qiyebao.application.orgmng;

import qiyebao.domain.orgmng.OrgRepository;
import qiyebao.domain.orgmng.*;
import org.springframework.stereotype.Service;

@Service
public class OrgService {
    private final OrgValidator orgValidator;
    private final OrgRepository orgRepository;

    public OrgService(
        OrgValidator orgValidator
        , OrgRepository orgRepository
    ) {

        this.orgValidator = orgValidator;
        this.orgRepository = orgRepository;
    }

    public OrgDto addOrg(OrgDto request, Long userId) {
        orgValidator.validate(request.getTenantId()
            , request.getId()
            , request.getLeaderId()
            , request.getSuperiorId()
            , request.getOrgTypeCode()
            , request.getName()
            , userId);
        Org org = buildOrg(request, userId);
        org = orgRepository.save(org);
        return buildOrgDto(org);
    }

    private OrgDto buildOrgDto(Org org) {
        OrgDto response = new OrgDto();
        response.setId(org.getId());
        response.setTenantId(org.getTenantId());
        response.setOrgTypeCode(org.getOrgTypeCode());
        response.setName(org.getName());
        response.setLeaderId(org.getLeaderId());
        response.setSuperiorId(org.getSuperiorId());
        response.setCreatedBy(org.getCreatedBy());
        response.setCreatedAt(org.getCreatedAt());
        response.setLastUpdatedBy(org.getLastUpdatedBy());
        response.setLastUpdatedAt(org.getLastUpdatedAt());
        return response;
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
