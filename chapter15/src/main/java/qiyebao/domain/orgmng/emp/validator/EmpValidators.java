package qiyebao.domain.orgmng.emp.validator;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.org.validator.OrgNameValidator;
import qiyebao.domain.orgmng.org.validator.OrgValidator;
import qiyebao.domain.tenantmng.TenantValidator;
import qiyebao.domain.usermng.UserValidator;

@Component
public class EmpValidators {

    private final TenantValidator tenantValidator;
    private final UserValidator userValidator;
    private final OrgValidator orgValidator;
    private final IdNumValidator idNumValidator;
    private final EmpNameValidator empNameValidator;
    private final DobValidator dobValidator;

    private final PostValidator postValidator;
    private final SkillValidator skillValidator;

    public EmpValidators(TenantValidator tenantValidator
        , UserValidator userValidator
        , OrgValidator orgValidator
        , IdNumValidator idNumValidator
        , EmpNameValidator empNameValidator
        , DobValidator dobValidator
        , PostValidator postValidator
        , SkillValidator skillValidator
    ) {
        this.tenantValidator = tenantValidator;
        this.userValidator = userValidator;
        this.orgValidator = orgValidator;
        this.idNumValidator = idNumValidator;
        this.empNameValidator = empNameValidator;
        this.postValidator = postValidator;
        this.skillValidator = skillValidator;
        this.dobValidator = dobValidator;
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

    public IdNumValidator idNum() {
        return idNumValidator;
    }

    public PostValidator post() {
        return postValidator;
    }

    public SkillValidator skill() {
        return skillValidator;
    }

    public EmpNameValidator empName() {
        return empNameValidator;
    }

    public DobValidator dob() {
        return dobValidator;
    }
}