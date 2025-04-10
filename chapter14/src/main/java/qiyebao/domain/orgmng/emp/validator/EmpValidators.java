package qiyebao.domain.orgmng.emp.validator;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.org.validator.OrgValidator;
import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;

@Component
public class EmpValidators {

    private final TenantValidator tenantValidator;
    private final UserValidator userValidator;
    private final OrgValidator orgValidator;
    private final PostValidator postValidator;

    public EmpValidators(TenantValidator tenantValidator
        , UserValidator userValidator
        , OrgValidator orgValidator
        , PostValidator postValidator
    ) {
        this.tenantValidator = tenantValidator;
        this.userValidator = userValidator;
        this.orgValidator = orgValidator;
        this.postValidator = postValidator;
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

    public PostValidator post() {
        return postValidator;
    }

    public SkillValidator skill() {
        return skillValidator;
    }
}