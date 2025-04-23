package qiyebao.domain.orgmng.org;

import qiyebao.domain.orgmng.org.validator.OrgValidators;

import java.time.LocalDateTime;

public class OrgBuilder {
    private final OrgValidators expect;

    private Long tenantId;
    private Long superiorId;
    private String orgTypeCode;
    private Long leaderId;
    private String name;
    private Long createdBy;

    public OrgBuilder(OrgValidators expect) {
        this.expect = expect;
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

        Org org = new Org(null
            , tenantId
            , orgTypeCode
            , LocalDateTime.now()
            , createdBy);
        org.setSuperiorId(superiorId);
        org.setLeaderId(leaderId);
        org.setName(name);
        org.setStatus(Org.Status.EFFECTIVE);
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
        expect.tenant().shouldValid(tenantId);
        expect.user().shouldValid(tenantId, createdBy);
    }

    // 校验组织负责人
    private void validateOrgLeader() {
        expect.orgLeader().shouldValid(tenantId, leaderId);
    }

    // 校验组织名称
    private void validateOrgName() {
        expect.orgName().shouldNotBlank(name);
        expect.orgName().underSameSuperiorShouldNotDuplicated(tenantId
            , superiorId
            , name);
    }

    // 校验组织类型
    private void validateOrgType() {
        expect.orgType().shouldNotBlank(orgTypeCode);
        expect.orgType().shouldValid(tenantId, orgTypeCode);
        expect.orgType().shouldNotEntp(orgTypeCode);
    }

    // 校验上级组织
    private void validateSuperior() {
        Org superior = expect.orgSuperior().shouldValid(tenantId, superiorId);
        String superiorOrgTypeCode = superior.getOrgTypeCode();

        expect.orgSuperior().orgTypeShouldValid(tenantId
            , superiorId
            , superiorOrgTypeCode);
        expect.orgSuperior().ofDevGrpShouldDevCent(
            orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
        expect.orgSuperior().ofDevCentAndDirectDeptShouldEntp(
            orgTypeCode
            , superiorId
            , superiorOrgTypeCode);
    }
}
