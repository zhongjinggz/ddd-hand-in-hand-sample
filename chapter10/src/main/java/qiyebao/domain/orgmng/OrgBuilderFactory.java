package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;

@Component
public class OrgBuilderFactory {
    OrgValidator orgValidator;

    public OrgBuilderFactory(OrgValidator orgValidator) {
        this.orgValidator = orgValidator;
    }
    public OrgBuilder newBuilder() {
        return new OrgBuilder(orgValidator);
    }
}
