package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;
import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;

@Component
public class OrgBuilderFactory {

    private final TenantValidator expectTenant;
    private final UserValidator expectUser;
    private final OrgTypeValidator expectOrgType;
    private final OrgSuperiorValidator expectOrgSuperior;
    private final OrgNameValidator expectOrgName;
    private final OrgLeaderValidator expectOrgLeader;

    public OrgBuilderFactory(TenantValidator expectTenant
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

    public OrgBuilder newBuilder() {
        return new OrgBuilder(expectTenant
            , expectUser
            , expectOrgType
            , expectOrgSuperior
            , expectOrgName
            , expectOrgLeader);
    }
}
