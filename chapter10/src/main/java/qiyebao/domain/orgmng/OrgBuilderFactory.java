package qiyebao.domain.orgmng;

import org.springframework.stereotype.Component;

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
