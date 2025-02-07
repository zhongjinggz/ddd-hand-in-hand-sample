package qiyebao.application.orgmng;

import qiyebao.domain.orgmng.OrgRepository;
import qiyebao.domain.orgmng.*;
import org.springframework.stereotype.Service;

@Service
public class OrgService {
    private final OrgRepository orgRepository;
    private final OrgFactory orgFactory;

    public OrgService(
        OrgRepository orgRepository
        , OrgFactory orgFactory
    ) {

        this.orgRepository = orgRepository;
        this.orgFactory = orgFactory;
    }

    public OrgDto addOrg(OrgDto request, Long userId) {
        Org org = orgFactory.build(
            request.getTenantId()
            , request.getOrgTypeCode()
            , request.getSuperiorId()
            , request.getLeaderId()
            , request.getName()
            , userId);
        return buildOrgDto(orgRepository.save(org));
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

}
