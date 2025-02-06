package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;
import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;

@Component
public class OrgValidator {
    private final TenantValidator tenantValidator;
    private final UserValidator userValidator;
    private final OrgTypeValidator orgTypeValidator;
    private final OrgSuperiorValidator orgSuperiorValidator;
    private final OrgNameValidator orgNameValidator;
    private final OrgLeaderValidator orgLeaderValidator;

    public OrgValidator(TenantValidator tenantValidator
        , UserValidator userValidator
        , OrgTypeValidator orgTypeValidator
        , OrgSuperiorValidator orgSuperiorValidator
        , OrgNameValidator orgNameValidator
        , OrgLeaderValidator orgLeaderValidator
    ) {
        this.tenantValidator = tenantValidator;
        this.userValidator = userValidator;
        this.orgTypeValidator = orgTypeValidator;
        this.orgSuperiorValidator = orgSuperiorValidator;
        this.orgNameValidator = orgNameValidator;
        this.orgLeaderValidator = orgLeaderValidator;
    }

    public void validate(Long tenantId
        , Long id
        , Long leaderId
        , Long superiorId
        , String orgTypeCode
        , String name
        , Long userId) {

        validateCommonInfo(userId, tenantId);
        validateOrgLeader(tenantId, leaderId);
        validateOrgName(tenantId, superiorId, name);
        validateOrgType(tenantId, orgTypeCode);
        validateSuperior(tenantId, id, orgTypeCode, superiorId);
    }


    // 校验上级组织
    public void validateSuperior(Long tenantId
        , Long id
        , String orgTypeCode
        , Long superiorId
    ) {
        Org superior = orgSuperiorValidator.superiorShouldValid(tenantId, superiorId);
        String superiorOrgTypeCode = superior.getOrgTypeCode();

        orgSuperiorValidator.superiorOrgTypeShouldValid(tenantId
            , superiorId
            , superiorOrgTypeCode, this);
        orgSuperiorValidator.superiorOfDevGrpShouldDevCent(id
            , orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
        orgSuperiorValidator.superiorOfDevCentAndDirectDeptShouldEntp(id
            , orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
    }

    // 校验组织类型
    public void validateOrgType(Long tenantId, String orgTypeCode) {
        orgTypeValidator.orgTypeShouldNotBlank(orgTypeCode);
        orgTypeValidator.orgTypeShouldValid(tenantId, orgTypeCode);
        orgTypeValidator.orgTypeShouldNotEntp(orgTypeCode);
    }

    // 校验组织名称
    public void validateOrgName(Long tenantId, Long superiorId, String name) {
        orgNameValidator.orgNameShouldNotBlank(name);
        orgNameValidator.orgNameUnderSameSuperiorShouldNotDuplicated(tenantId
            , superiorId
            , name);
    }

    // 校验组织负责人
    public void validateOrgLeader(Long tenantId, Long leaderId) {
        orgLeaderValidator.orgLeaderShouldValid(tenantId, leaderId);
    }

    // 校验通用信息
    public void validateCommonInfo(Long userId, Long tenantId) {
        tenantValidator.tenantShouldValid(tenantId);
        userValidator.userShouldValid(tenantId, userId);
    }

}
