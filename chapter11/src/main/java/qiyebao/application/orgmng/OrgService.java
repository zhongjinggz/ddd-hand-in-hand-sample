package qiyebao.application.orgmng;

import org.springframework.transaction.annotation.Transactional;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.org.OrgHandler;
import qiyebao.domain.orgmng.org.OrgRepository;
import org.springframework.stereotype.Service;
import qiyebao.domain.orgmng.org.Org;
import qiyebao.domain.orgmng.org.OrgBuilderFactory;

@Service
public class OrgService {
    private final OrgRepository orgRepository;
    private final OrgBuilderFactory orgBuilderFactory;
    private final OrgHandler orgHandler;

    public OrgService(OrgRepository orgRepository
        , OrgBuilderFactory orgBuilderFactory
        , OrgHandler orgHandler
    ) {

        this.orgRepository = orgRepository;
        this.orgBuilderFactory = orgBuilderFactory;
        this.orgHandler = orgHandler;
    }

    @Transactional
    public OrgResponse addOrg(AddOrgRequest request, Long userId) {
        Org org = orgBuilderFactory.newBuilder()
            .tenantId(request.getTenantId())
            .orgTypeCode(request.getOrgTypeCode())
            .superiorId(request.getSuperiorId())
            .leaderId(request.getLeaderId())
            .name((request.getName()))
            .createdBy(userId)
            .build();

        return buildOrgResponse(orgRepository.add(org));
    }


    @Transactional
    public OrgResponse modifyOrg(Long id, ModifyOrgRequest request, Long userId) {
        Org org = orgRepository.findById(request.getTenantId(), id)
            .orElseThrow(() -> new BusinessException(
                "要修改的组织(id =" + id + "  )不存在！"));

        orgHandler.modify(org
            , request.getName()
            , request.getLeaderId()
            , userId);

        orgRepository.modify(org);
        return buildOrgResponse(org);
    }


    @Transactional
    public Long cancelOrg(Long id, Long tenantId, Long userId) {
        Org org = orgRepository.findById(tenantId, id)
            .orElseThrow(() -> new BusinessException(
                "要取消的组织(id =" + id + "  )不存在！"));

        orgHandler.cancel(org, userId);
        orgRepository.modify(org);

        return org.getId();
    }


    private OrgResponse buildOrgResponse(Org org) {
        OrgResponse response = new OrgResponse();
        response.setId(org.getId());
        response.setTenantId(org.getTenantId());
        response.setOrgTypeCode(org.getOrgTypeCode());
        response.setName(org.getName());
        response.setLeaderId(org.getLeaderId());
        response.setSuperiorId(org.getSuperiorId());
        response.setCreatedBy(org.getCreatedBy());
        response.setCreatedAt(org.getCreatedAt());
        response.setLastUpdatedBy(org.getUpdatedBy());
        response.setLastUpdatedAt(org.getUpdatedAt());
        return response;
    }

}
