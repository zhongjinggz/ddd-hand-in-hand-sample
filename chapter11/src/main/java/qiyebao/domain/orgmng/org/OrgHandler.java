package qiyebao.domain.orgmng.org;

import qiyebao.domain.orgmng.org.validator.OrgValidators;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class OrgHandler {
    private final OrgValidators expect;

    public OrgHandler( OrgValidators expect) {
        this.expect = expect;
    }

    public void modify(Org org
        , String newName
        , Long newLeaderId
        , Long userId
    ) {
        modifyName(org, newName);
        modifyLeader(org, newLeaderId);
        modifyAuditInfo(org, userId);
    }

    public void cancel(Org org, Long userId) {
        expect.orgToBeCancelled().shouldNotHasEmp(org.getTenantId(), org.getId());
        expect.orgToBeCancelled().shouldBeEffective(org);
        org.setStatus(OrgStatus.CANCELLED);
        modifyAuditInfo(org, userId);
    }

    private void modifyLeader(Org org, Long newLeaderId) {
        if (newLeaderId != null && !newLeaderId.equals(org.getLeaderId())) {
            expect.orgLeader().shouldValid(org.getTenantId(), newLeaderId);
            org.setLeaderId(newLeaderId);
        }
    }

    private void modifyName(Org org, String newName) {
        if (newName != null && !newName.equals(org.getName())) {
            expect.orgName().shouldNotBlank(newName);
            expect.orgName().underSameSuperiorShouldNotDuplicated(org.getTenantId()
                , org.getSuperiorId()
                , newName);
            org.setName(newName);
        }
    }

    private void modifyAuditInfo(Org org, Long userId) {
        org.setLastUpdatedBy(userId);
        org.setLastUpdatedAt(LocalDateTime.now());
    }
}
