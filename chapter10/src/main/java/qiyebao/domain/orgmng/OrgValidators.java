package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;
import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;

@Component
public class OrgValidators {

    private final TenantValidator tenantValidator;
    private final UserValidator userValidator;
    private final OrgTypeValidator orgTypeValidator;
    private final OrgSuperiorValidator orgSuperiorValidator;
    private final OrgNameValidator orgNameValidator;
    private final OrgLeaderValidator orgLeaderValidator;

    public OrgValidators(TenantValidator tenantValidator
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

    TenantValidator tenant() {
        return tenantValidator;
    }

    UserValidator user() {
        return userValidator;
    }

    OrgTypeValidator orgType() {
        return orgTypeValidator;
    }

    OrgSuperiorValidator orgSuperior() {
        return orgSuperiorValidator;
    }

    OrgNameValidator orgName() {
        return orgNameValidator;
    }

    OrgLeaderValidator orgLeader() {
        return orgLeaderValidator;
    }
}