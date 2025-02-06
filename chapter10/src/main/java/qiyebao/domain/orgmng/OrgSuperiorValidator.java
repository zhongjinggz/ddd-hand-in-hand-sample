package qiyebao.domain.orgmng;

import qiyebao.common.framework.exception.BusinessException;

public class OrgSuperiorValidator {
    private final OrgRepository orgRepository;
    private final OrgTypeRepository orgTypeRepository;

    public OrgSuperiorValidator(OrgRepository orgRepository
        , OrgTypeRepository orgTypeRepository
    ) {
        this.orgRepository = orgRepository;
        this.orgTypeRepository = orgTypeRepository;
    }

    // 上级组织应当有效
    public Org superiorShouldValid(Long tenantId, Long superiorId) {
        return orgRepository.findByIdAndStatus(tenantId
                , superiorId
                , OrgStatus.EFFECTIVE)
            .orElseThrow(() ->
                new BusinessException(
                    String.format("'%s' 不是有效的组织 id !", superiorId)
                )
            );
    }

    // 上级组织的组织类型应当有效
    public void superiorOrgTypeShouldValid(Long tenantId
        , Long superiorId
        , String superiorOrgTypeCode, OrgValidator orgValidator) {

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
    public void superiorOfDevCentAndDirectDeptShouldEntp(Long id
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
    public void superiorOfDevGrpShouldDevCent(Long id
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
