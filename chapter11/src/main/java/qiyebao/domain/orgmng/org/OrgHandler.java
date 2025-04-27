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

    public void cancel(Org org, Long userId) {
        expect.orgToBeCancelled().shouldNotHasEmp(org.getTenantId(), org.getId());

        org.cancel();  // 不再直接修改Org的状态
        modifyAuditInfo(org, userId);
    }

    public void modifyLeader(Org org, Long newLeaderId) {
        if (newLeaderId != null && !newLeaderId.equals(org.getLeaderId())) {
            expect.orgLeader().shouldValid(org.getTenantId(), newLeaderId);
            org.setLeaderId(newLeaderId);
        }
    }

    public void modifyName(Org org, String newName) {
        if (newName != null && !newName.equals(org.getName())) {
            expect.orgName().shouldNotBlank(newName);
            expect.orgName().underSameSuperiorShouldNotDuplicated(org.getTenantId()
                , org.getSuperiorId()
                , newName);
            org.setName(newName);
        }
    }

    public void modifyAuditInfo(Org org, Long userId) {
        org.setUpdatedBy(userId);
        org.setUpdatedAt(LocalDateTime.now());
    }
}
