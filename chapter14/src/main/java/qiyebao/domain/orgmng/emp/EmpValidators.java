package qiyebao.domain.orgmng.emp;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.org.validator.OrgValidator;
import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;

@Component
public class EmpValidators {

    private final TenantValidator tenantValidator;
    private final UserValidator userValidator;
    private final OrgValidator orgValidator;

    public EmpValidators(TenantValidator tenantValidator
        , UserValidator userValidator
        , OrgValidator orgValidator
    ) {
        this.tenantValidator = tenantValidator;
        this.userValidator = userValidator;
        this.orgValidator = orgValidator;
    }

    public TenantValidator tenant() {
        return tenantValidator;
    }

    public UserValidator user() {
        return userValidator;
    }

    public OrgValidator org() {
        return orgValidator;
    }

}