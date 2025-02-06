package qiyebao.application.orgmng;

import qiyebao.domain.orgmng.OrgRepository;
import qiyebao.domain.orgmng.OrgTypeRepository;
import qiyebao.adapter.driven.persistence.tenantmng.TenantRepository;
import qiyebao.adapter.driven.persistence.usermng.UserRepository;
import qiyebao.domain.orgmng.*;
import qiyebao.domain.orgmng.EmpRepository;
import org.springframework.stereotype.Service;

@Service
public class OrgService {
    private final OrgValidator orgValidator;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final OrgTypeRepository orgTypeRepository;
    private final OrgRepository orgRepository;
    private final EmpRepository empRepository;

    public OrgService(
        OrgValidator orgValidator
        , UserRepository userRepository
        , TenantRepository tenantRepository
        , OrgRepository orgRepository
        , EmpRepository empRepository
        , OrgTypeRepository orgTypeRepository) {

        this.orgValidator = orgValidator;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.orgRepository = orgRepository;
        this.empRepository = empRepository;
        this.orgTypeRepository = orgTypeRepository;
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
