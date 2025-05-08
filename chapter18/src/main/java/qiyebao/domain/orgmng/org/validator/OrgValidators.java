package qiyebao.domain.orgmng.org.validator;

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
    private final OrgToBeCancelledValidator orgToBeCancelledValidator;

    public OrgValidators(TenantValidator tenantValidator
        , UserValidator userValidator
        , OrgTypeValidator orgTypeValidator
        , OrgSuperiorValidator orgSuperiorValidator
        , OrgNameValidator orgNameValidator
        , OrgLeaderValidator orgLeaderValidator
        , OrgToBeCancelledValidator orgToBeCancelledValidator
    ) {
        this.tenantValidator = tenantValidator;
        this.userValidator = userValidator;
        this.orgTypeValidator = orgTypeValidator;
        this.orgSuperiorValidator = orgSuperiorValidator;
        this.orgNameValidator = orgNameValidator;
        this.orgLeaderValidator = orgLeaderValidator;
        this.orgToBeCancelledValidator = orgToBeCancelledValidator;
    }

    public TenantValidator tenant() {
        return tenantValidator;
    }

    public UserValidator user() {
        return userValidator;
    }

    public OrgTypeValidator orgType() {
        return orgTypeValidator;
    }

    public OrgSuperiorValidator orgSuperior() {
        return orgSuperiorValidator;
    }

    public OrgNameValidator orgName() {
        return orgNameValidator;
    }

    public OrgLeaderValidator orgLeader() {
        return orgLeaderValidator;
    }

    public OrgToBeCancelledValidator orgToBeCancelled() {
        return orgToBeCancelledValidator;
    }
}