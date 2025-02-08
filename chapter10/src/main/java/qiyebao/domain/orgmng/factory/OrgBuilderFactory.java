package qiyebao.domain.orgmng.factory;

import org.springframework.stereotype.Component;
import qiyebao.domain.orgmng.domainservice.OrgValidators;

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
