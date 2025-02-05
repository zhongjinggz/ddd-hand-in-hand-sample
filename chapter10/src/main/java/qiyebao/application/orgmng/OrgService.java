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

        validateCommonInfo(userId, tenantId);
        validateOrgLeader(tenantId, request.getLeaderId());
        validateOrgName(tenantId, request.getSuperiorId(), request.getName());
        validateOrgType(tenantId, request.getOrgTypeCode());
        validateSuperior(tenantId, request.getId(), request.getOrgTypeCode(), request.getSuperiorId());
    }

    // 校验通用信息
    private void validateCommonInfo(Long userId, Long tenantId) {
        tenantShouldValid(tenantId);
        userShouldValid(tenantId, userId);
    }

    // 校验组织负责人
    private void validateOrgLeader(Long tenantId, Long leaderId) {
        orgLeaderShouldValid(tenantId, leaderId);
    }

    // 校验组织名称
    private void validateOrgName(Long tenantId, Long superiorId, String name) {
        orgNameShouldNotBlank(name);
        orgNameUnderSameSuperiorShouldNotDuplicated(tenantId
            , superiorId
            , name);
    }

    // 校验组织类型
    private void validateOrgType(Long tenantId, String orgTypeCode) {
        orgTypeShouldNotBlank(orgTypeCode);
        orgTypeShouldValid(tenantId, orgTypeCode);
        orgTypeShouldNotEntp(orgTypeCode);
    }

    // 校验上级组织
    private void validateSuperior(Long tenantId, Long id, String orgTypeCode, Long superiorId) {
        Org superior = superiorShouldValid(tenantId, superiorId);
        String superiorOrgTypeCode = superior.getOrgTypeCode();

        superiorOrgTypeShouldValid(tenantId
            , superiorId
            , superiorOrgTypeCode);
        superiorOfDevGrpShouldDevCent(id
            , orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
        superiorOfDevCentAndDirectDeptShouldEntp(id
            , orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
    }

    // 租户应当有效
    private void tenantShouldValid(Long tenantId) {
        if (!tenantRepository.existsByIdAndStatus(tenantId, TenantStatus.EFFECTIVE)) {
            throw new BusinessException(
                String.format("id为'%s'的租户不是有效租户！", tenantId)
            );
        }
    }

    // 用户应当有效
    private void userShouldValid(Long tenantId, Long userId) {
        if (!userRepository.existsByIdAndStatus(tenantId, userId
            , UserStatus.EFFECTIVE)) {
            throw new BusinessException(
                String.format("id为'%s'的用户不是有效用户！", userId)
            );
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
                String.format("组织负责人(id='%s')不是在职员工！", leaderId)
            );
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
                String.format("同一上级下已经有名为'%s'的组织存在！", name)
            );
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
                String.format("'%s'不是有效的组织类别代码！", orgTypeCode));
        }
    }

    // 组织类型不应是企业（企业是在创建租户的时候添加的，因此不能单独添加企业）
    private void orgTypeShouldNotEntp(String orgTypeCode) {
        if ("ENTP".equals(orgTypeCode)) {
            throw new BusinessException(
                "企业是在创建租户的时候添加的，因此不能单独添加企业!");
        }
    }

    // 上级组织应当有效
    private Org superiorShouldValid(Long tenantId, Long superiorId) {
        Org superior = orgRepository.findByIdAndStatus(tenantId
                , superiorId
                , OrgStatus.EFFECTIVE)
            .orElseThrow(() ->
                new BusinessException(
                    String.format("'%s' 不是有效的组织 id !", superiorId)
                )
            );
        return superior;
    }

    // 上级组织的组织类型应当有效
    private void superiorOrgTypeShouldValid(Long tenantId
        , Long superiorId, String superiorOrgTypeCode) {
        if (orgTypeRepository.existsByCodeAndStatus(tenantId
            , superiorOrgTypeCode
            , OrgTypeStatus.EFFECTIVE)) {
            throw (
                new BusinessException(
                    String.format("id 为 '%s' 的组织的组织类型代码 '%s' 无效!"
                        , superiorId
                        , superiorOrgTypeCode)
                )
            );
        }
    }

    // 开发中心和直属部门的上级只能是企业
    private void superiorOfDevCentAndDirectDeptShouldEntp(Long id
        , String orgTypeCode
        , Long superiorId
        , String superiorOrgTypeCode) {
        if (
            ("DEVCENT".equals(orgTypeCode) || "DIRDEP".equals(orgTypeCode)
            ) && !"ENTP".equals(superiorOrgTypeCode)
        ) {
            throw new BusinessException(
                String.format("开发中心或直属部门(id = '%s') 的上级(id = '%s')不是企业！"
                    , id
                    , superiorId)
            );
        }
    }

    // 开发组的上级只能是开发中心
    private void superiorOfDevGrpShouldDevCent(Long id
        , String orgTypeCode
        , Long superiorId
        , String superiorOrgTypeCode) {
        if ("DEVGRP".equals(orgTypeCode)
            && !"DEVCENT".equals(superiorOrgTypeCode)) {
            throw new BusinessException(
                String.format("开发组(id = '%s') 的上级(id = '%s')不是开发中心！"
                    , id
                    , superiorId)
            );
        }
    }
}
