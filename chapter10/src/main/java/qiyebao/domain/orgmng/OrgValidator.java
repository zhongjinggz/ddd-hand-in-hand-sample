package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;
import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;

@Component
public class OrgValidator {
    private final TenantValidator expectTenant;
    private final UserValidator expectUser;
    private final OrgTypeValidator expectOrgType;
    private final OrgSuperiorValidator expectOrgSuperior;
    private final OrgNameValidator expectOrgName;
    private final OrgLeaderValidator expectOrgLeader;

    public OrgValidator(TenantValidator expectTenant
        , UserValidator expectUser
        , OrgTypeValidator expectOrgType
        , OrgSuperiorValidator expectOrgSuperior
        , OrgNameValidator expectOrgName
        , OrgLeaderValidator expectOrgLeader
    ) {
        this.expectTenant = expectTenant;
        this.expectUser = expectUser;
        this.expectOrgType = expectOrgType;
        this.expectOrgSuperior = expectOrgSuperior;
        this.expectOrgName = expectOrgName;
        this.expectOrgLeader = expectOrgLeader;
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

    // 校验通用信息
    public void validateCommonInfo(Long userId, Long tenantId) {
        expectTenant.shouldValid(tenantId);
        expectUser.shouldValid(tenantId, userId);
    }

    // 校验组织负责人
    public void validateOrgLeader(Long tenantId, Long leaderId) {
        expectOrgLeader.shouldValid(tenantId, leaderId);
    }

    // 校验组织名称
    public void validateOrgName(Long tenantId, Long superiorId, String name) {
        expectOrgName.shouldNotBlank(name);
        expectOrgName.underSameSuperiorShouldNotDuplicated(tenantId
            , superiorId
            , name);
    }

    // 校验组织类型
    public void validateOrgType(Long tenantId, String orgTypeCode) {
        expectOrgType.shouldNotBlank(orgTypeCode);
        expectOrgType.shouldValid(tenantId, orgTypeCode);
        expectOrgType.shouldNotEntp(orgTypeCode);
    }

    // 校验上级组织
    public void validateSuperior(Long tenantId
        , Long id
        , String orgTypeCode
        , Long superiorId
    ) {
        Org superior = expectOrgSuperior.shouldValid(tenantId, superiorId);
        String superiorOrgTypeCode = superior.getOrgTypeCode();

        expectOrgSuperior.orgTypeShouldValid(tenantId
            , superiorId
            , superiorOrgTypeCode, this);
        expectOrgSuperior.ofDevGrpShouldDevCent(id
            , orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
        expectOrgSuperior.ofDevCentAndDirectDeptShouldEntp(id
            , orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
    }



}
