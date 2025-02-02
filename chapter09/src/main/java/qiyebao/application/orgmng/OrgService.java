package qiyebao.application.orgmng;

import qiyebao.adapter.driven.persistence.orgmng.OrgRepository;
import qiyebao.adapter.driven.persistence.orgmng.OrgTypeRepository;
import qiyebao.adapter.driven.persistence.tenantmng.TenantRepository;
import qiyebao.adapter.driven.persistence.usermng.UserRepository;
import qiyebao.adapter.driving.restful.orgmng.OrgDto;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.*;
import qiyebao.adapter.driven.persistence.orgmng.EmpRepository;
import qiyebao.domain.tenantmng.TenantStatus;
import qiyebao.domain.usermng.UserStatus;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class OrgService {
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final OrgTypeRepository orgTypeRepository;
    private final OrgRepository orgRepository;
    private final EmpRepository empRepository;

    public OrgService(UserRepository userRepository
            , TenantRepository tenantRepository
            , OrgRepository orgRepository
            , EmpRepository empRepository
            , OrgTypeRepository orgTypeRepository) {

        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.orgRepository = orgRepository;
        this.empRepository = empRepository;
        this.orgTypeRepository = orgTypeRepository;
    }

    public OrgDto addOrg(OrgDto request, Long userId) {
        validate(request);

        Org org = buildOrg(request, userId);

        org = orgRepository.save(org);

        return buildOrgDto(org);
    }

    private static OrgDto buildOrgDto(Org org) {
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

    private static Org buildOrg(OrgDto request, Long userId) {
        Org org = new Org();
        org.setOrgTypeCode(request.getOrgTypeCode());
        org.setLeaderId(request.getLeaderId());
        org.setName(request.getName());
        org.setSuperiorId(request.getSuperiorId());
        org.setTenantId(request.getTenantId());
        org.setCreatedBy(userId);
        return org;
    }

    private void validate(OrgDto request) {
        final var tenant = request.getTenantId();

        // 创建者必须是有效用户
        final var createdBy = request.getCreatedBy();

        if (!userRepository.existsByIdAndStatus(tenant, createdBy
                , UserStatus.EFFECTIVE)) {
            throw new BusinessException("id为'" + createdBy + "'的用户不是有效用户！");
        }

        // 租户必须有效
        if (!tenantRepository.existsByIdAndStatus(tenant, TenantStatus.EFFECTIVE)) {
            throw new BusinessException("id为'" + tenant + "'的租户不是有效租户！");
        }

        // 组织类别不能为空
        if (isBlank(request.getOrgTypeCode())) {
            throw new BusinessException("组织类别不能为空！");
        }

        // 企业是在创建租户的时候创建好的，因此不能单独创建企业
        if ("ENTP".equals(request.getOrgTypeCode())) {
            throw new BusinessException("企业是在创建租户的时候创建好的，因此不能单独创建企业!");
        }

        // 组织类别必须有效
        if (!orgTypeRepository.existsByCodeAndStatus(tenant, request.getOrgTypeCode(), OrgTypeStatus.EFFECTIVE)) {
            throw new BusinessException("'" + request.getOrgTypeCode() + "'不是有效的组织类别代码！");
        }

        // 上级组织应该是有效组织
        Org superior = orgRepository.findByIdAndStatus(tenant
                        , request.getSuperiorId(), OrgStatus.EFFECTIVE)
                .orElseThrow(() ->
                        new BusinessException("'" + request.getSuperiorId() + "' 不是有效的组织 id !"));


        // 开发组的上级只能是开发中心
        OrgType superiorOrgType = orgTypeRepository.findByCodeAndStatus(tenant
                        , superior.getOrgTypeCode()
                        , OrgTypeStatus.EFFECTIVE)
                .orElseThrow(() ->
                        new BusinessException("id 为 '" + request.getSuperiorId()
                                + "' 的组织的组织类型代码 '" + superior.getOrgTypeCode() + "' 无效!"));
        if ("DEVGRP".equals(request.getOrgTypeCode()) && !"DEVCENT".equals(superiorOrgType.getCode())) {
            throw new BusinessException("开发组(id = '" + request.getId()
                    + "') 的上级(id = '" + request.getSuperiorId()
                    + "')不是开发中心！");
        }


        // 开发中心和直属部门的上级只能是企业
        if (("DEVCENT".equals(request.getOrgTypeCode()) || "DIRDEP".equals(request.getOrgTypeCode()))
                && !"ENTP".equals(superiorOrgType.getCode())) {
            throw new BusinessException("开发中心或直属部门(id = '" + request.getId()
                    + "') 的上级(id = '" + request.getSuperiorId()
                    + "')不是企业！");
        }


        // 组织负责人可以空缺，如果有的话，的必须是一个在职员工（含试用期）
        if (request.getLeaderId() != null
                && !empRepository.existsByIdAndStatus(tenant, request.getLeaderId()
                , EmpStatus.REGULAR, EmpStatus.PROBATION)) {
            throw new BusinessException("组织负责人(id='" + request.getLeaderId() + "')不是在职员工！");
        }

        // 组织必须有名称
        if (isBlank(request.getName())) {
            throw new BusinessException("组织没有名称！");
        }

        // 同一个组织下的下级组织不能重名
        if (orgRepository.existsBySuperiorAndName(tenant, request.getSuperiorId(), request.getName())) {

            throw new BusinessException("同一上级下已经有名为'" + request.getName() + "'的组织存在！");
        }
    }

}
