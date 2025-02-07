package qiyebao.application.orgmng;

import qiyebao.domain.orgmng.OrgRepository;
import qiyebao.domain.orgmng.*;
import org.springframework.stereotype.Service;

@Service
public class OrgService {
    private final OrgRepository orgRepository;
    private final OrgBuilderFactory orgBuilderFactory;

    public OrgService(
        OrgRepository orgRepository
        , OrgBuilder orgBuilder
        , OrgBuilderFactory orgBuilderFactory
    ) {

        this.orgRepository = orgRepository;
        this.orgBuilderFactory = orgBuilderFactory;
    }

    public OrgDto addOrg(OrgDto request, Long userId) {
        Org org = orgBuilderFactory.newBuilder()
            .tenantId(request.getTenantId())
            .orgTypeCode(request.getOrgTypeCode())
            .superiorId(request.getSuperiorId())
            .leaderId(request.getLeaderId())
            .name((request.getName()))
            .createdBy(userId)
            .build();

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
