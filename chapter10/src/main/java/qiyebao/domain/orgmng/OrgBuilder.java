package qiyebao.domain.orgmng;

import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;
import java.time.LocalDateTime;

public class OrgBuilder {
    private final TenantValidator expectTenant;
    private final UserValidator expectUser;
    private final OrgTypeValidator expectOrgType;
    private final OrgSuperiorValidator expectOrgSuperior;
    private final OrgNameValidator expectOrgName;
    private final OrgLeaderValidator expectOrgLeader;

    private Long tenantId;
    private Long superiorId;
    private String orgTypeCode;
    private Long leaderId;
    private String name;
    private Long createdBy;

    public OrgBuilder(TenantValidator expectTenant
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

    public OrgBuilder tenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public OrgBuilder superiorId(Long superiorId) {
        this.superiorId = superiorId;
        return this;
    }

    public OrgBuilder orgTypeCode(String orgTypeCode) {
        this.orgTypeCode = orgTypeCode;
        return this;
    }

    public OrgBuilder leaderId(Long leaderId) {
        this.leaderId = leaderId;
        return this;
    }

    public OrgBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OrgBuilder createdBy(Long userId) {
        this.createdBy = userId;
        return this;
    }

    public Org build() {
        validate();

        Org org = new Org();
        org.setTenantId(tenantId);
        org.setOrgTypeCode(orgTypeCode);
        org.setSuperiorId(superiorId);
        org.setLeaderId(leaderId);
        org.setName(name);
        org.setStatus(OrgStatus.EFFECTIVE);
        org.setCreatedBy(createdBy);
        org.setCreatedAt(LocalDateTime.now());
        return org;
    }

    private void validate() {
        validateCommonInfo();
        validateOrgLeader();
        validateOrgName();
        validateOrgType();
        validateSuperior();
    }

    // 校验通用信息
    private void validateCommonInfo() {
        expectTenant.shouldValid(tenantId);
        expectUser.shouldValid(tenantId, createdBy);
    }

    // 校验组织负责人
    private void validateOrgLeader() {
        expectOrgLeader.shouldValid(tenantId, leaderId);
    }

    // 校验组织名称
    private void validateOrgName() {
        expectOrgName.shouldNotBlank(name);
        expectOrgName.underSameSuperiorShouldNotDuplicated(tenantId
            , superiorId
            , name);
    }

    // 校验组织类型
    private void validateOrgType() {
        expectOrgType.shouldNotBlank(orgTypeCode);
        expectOrgType.shouldValid(tenantId, orgTypeCode);
        expectOrgType.shouldNotEntp(orgTypeCode);
    }

    // 校验上级组织
    private void validateSuperior() {
        Org superior = expectOrgSuperior.shouldValid(tenantId, superiorId);
        String superiorOrgTypeCode = superior.getOrgTypeCode();

        expectOrgSuperior.orgTypeShouldValid(tenantId
            , superiorId
            , superiorOrgTypeCode );
        expectOrgSuperior.ofDevGrpShouldDevCent(
            orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
        expectOrgSuperior.ofDevCentAndDirectDeptShouldEntp(
            orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
    }
}
