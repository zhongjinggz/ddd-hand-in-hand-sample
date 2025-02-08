package qiyebao.domain.orgmng.org;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.org.validator.OrgValidators;

@Component
public class OrgBuilderFactory {
    private final OrgValidators orgValidators;

    public OrgBuilderFactory(OrgValidators orgValidators) {
        this.orgValidators = orgValidators;
    }

    public OrgBuilder newBuilder() {
        return new OrgBuilder(orgValidators);
    }
}
