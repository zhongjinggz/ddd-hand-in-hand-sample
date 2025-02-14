package qiyebao.adapter.driving.restful.orgmng;

import org.springframework.web.bind.annotation.*;
import qiyebao.application.orgmng.OrgDto;
import qiyebao.application.orgmng.OrgService;

@RestController
public class OrgController {
    private final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @PostMapping("/api/organizations")
    public OrgDto addOrg(@RequestBody OrgDto request
        , @RequestParam("userid") Long userId
    ) {
        return orgService.addOrg(request, userId);
    }

    @PatchMapping("/api/organizations/{id}")
    public OrgDto modifyOrg(@PathVariable Long id
        , @RequestBody OrgDto request
        , @RequestParam("userid") Long userId) {
        return orgService.modifyOrg(id, request, userId);
    }

    @PostMapping("/api/organizations/{id}/cancel")
    public Long cancelOrg(@PathVariable Long id
        , @RequestParam Long tenant
        , @RequestParam Long userId) {
        return orgService.cancelOrg(id, tenant, userId);
    }
}
