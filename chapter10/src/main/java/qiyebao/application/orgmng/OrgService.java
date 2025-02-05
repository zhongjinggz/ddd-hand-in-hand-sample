package qiyebao.application.orgmng;

import qiyebao.domain.orgmng.OrgRepository;
import qiyebao.domain.orgmng.OrgTypeRepository;
import qiyebao.adapter.driven.persistence.tenantmng.TenantRepository;
import qiyebao.adapter.driven.persistence.usermng.UserRepository;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.orgmng.*;
import qiyebao.domain.orgmng.EmpRepository;
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
        validate(request, userId);
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

    private void validate(OrgDto request, Long userId) {
        final var tenantId = request.getTenantId();

        // 校验通用信息
        tenantShouldValid(tenantId);
        userShouldValid(tenantId, userId);

        // 校验组织负责人
        orgLeaderShouldValid(tenantId, request.getLeaderId());

        // 校验组织名称
        orgNameShouldNotBlank(request.getName());
        orgNameUnderSameSuperiorShouldNotDuplicated(tenantId
                , request.getSuperiorId()
                , request.getName());

        // 校验组织类型
        orgTypeShouldNotBlank(request.getOrgTypeCode());
        orgTypeShouldValid(tenantId, request.getOrgTypeCode());
        orgTypeShouldNotEntp(request.getOrgTypeCode());

        // 校验上级组织
        Org superior = superiorShouldValid(tenantId, request.getSuperiorId());
        OrgType superiorOrgType = superiorOrgTypeShouldValid(tenantId
                , superior.getOrgTypeCode()
                , request.getSuperiorId());
        superiorOfDevGrpShouldDevCent(request.getId()
                , request.getOrgTypeCode()
                , request.getSuperiorId()
                , superiorOrgType.getCode());
        superiorOfDevCentAndDirectDeptShouldEntp(request.getId()
                , request.getOrgTypeCode()
                , request.getSuperiorId()
                , superiorOrgType.getCode());
    }


    // 租户应当有效
    private void tenantShouldValid(Long tenantId) {
        if (!tenantRepository.existsByIdAndStatus(tenantId, TenantStatus.EFFECTIVE)) {
            throw new BusinessException(
                    "id为'" + tenantId + "'的租户不是有效租户！");
        }
    }

    // 用户应当有效
    private void userShouldValid(Long tenantId, Long userId) {
        if (!userRepository.existsByIdAndStatus(tenantId, userId
                , UserStatus.EFFECTIVE)) {
            throw new BusinessException(
                    "id为'" + userId + "'的用户不是有效用户！");
        }
    }

    // 组织负责人可以空缺，如果有的话，的必须是一个在职员工（含试用期）
    private void orgLeaderShouldValid(Long tenantId, Long leaderId) {
        if (leaderId != null
                && !empRepository.existsByIdAndStatus(tenantId
                , leaderId
                , EmpStatus.REGULAR
                , EmpStatus.PROBATION)) {
            throw new BusinessException(
                    "组织负责人(id='" + leaderId + "')不是在职员工！");
        }
    }

    // 组织名称不能为空
    private void orgNameShouldNotBlank(String name) {
        if (isBlank(name)) {
            throw new BusinessException("组织没有名称！");
        }
    }

    // 同一上级下的(直属）组织的名称不能重复
    private void orgNameUnderSameSuperiorShouldNotDuplicated(Long tenantId
            , Long superiorId
            , String name) {
        if (orgRepository.existsBySuperiorAndName(tenantId
                , superiorId
                , name)) {
            throw new BusinessException(
                    "同一上级下已经有名为'" + name + "'的组织存在！");
        }
    }

    // 组织类别不能为空
    private void orgTypeShouldNotBlank(String orgTypeCode) {
        if (isBlank(orgTypeCode)) {
            throw new BusinessException("组织类别不能为空！");
        }
    }

    // 组织类别应当有效
    private void orgTypeShouldValid(Long tenantId, String orgTypeCode) {
        if (!orgTypeRepository.existsByCodeAndStatus(tenantId
                , orgTypeCode
                , OrgTypeStatus.EFFECTIVE)) {
            throw new BusinessException(
                    "'" + orgTypeCode + "'不是有效的组织类别代码！");
        }
    }

    // 组织类型不应是企业（企业是在创建租户的时候添加的，因此不能单独添加企业）
    private void orgTypeShouldNotEntp(String orgTypeCode) {
        if ("ENTP".equals(orgTypeCode)) {
            throw new BusinessException(
                    "企业是在创建租户的时候创建好的，因此不能单独创建企业!");
        }
    }

    // 上级组织应当有效
    private Org superiorShouldValid(Long tenantId, Long superiorId) {
        Org superior = orgRepository.findByIdAndStatus(tenantId
                        , superiorId
                        , OrgStatus.EFFECTIVE)
                .orElseThrow(() ->
                        new BusinessException(
                                "'" + superiorId + "' 不是有效的组织 id !"));
        return superior;
    }

    // 上级组织的组织类型应当有效
    private OrgType superiorOrgTypeShouldValid(Long tenantId
            , String superiorOrgTypeCode
            , Long superiorId) {
        OrgType superiorOrgType = orgTypeRepository.findByCodeAndStatus(tenantId
                        , superiorOrgTypeCode
                        , OrgTypeStatus.EFFECTIVE)
                .orElseThrow(() ->
                        new BusinessException("id 为 '" + superiorId
                                + "' 的组织的组织类型代码 '" + superiorOrgTypeCode
                                + "' 无效!"));
        return superiorOrgType;
    }

    // 开发中心和直属部门的上级只能是企业
    private void superiorOfDevCentAndDirectDeptShouldEntp(Long id
            , String orgTypeCode
            , Long superiorId
            , String superiorOrgTypeCode) {
        if (
                ("DEVCENT".equals(orgTypeCode)
                        || "DIRDEP".equals(orgTypeCode)
                ) && !"ENTP".equals(superiorOrgTypeCode)
        ) {
            throw new BusinessException("开发中心或直属部门(id = '" + id
                    + "') 的上级(id = '" + superiorId
                    + "')不是企业！");
        }
    }

    // 开发组的上级只能是开发中心
    private void superiorOfDevGrpShouldDevCent(Long id
            , String orgTypeCode
            , Long superiorId
            , String superiorOrgTypeCode) {
        if ("DEVGRP".equals(orgTypeCode)
                && !"DEVCENT".equals(superiorOrgTypeCode)) {
            throw new BusinessException("开发组(id = '" + id
                    + "') 的上级(id = '" + superiorId
                    + "')不是开发中心！");
        }
    }

}
