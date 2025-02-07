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

    // 校验通用信息

    // 校验组织负责人

    // 校验组织名称

    // 校验组织类型

    // 校验上级组织

}
