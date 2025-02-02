package qiyebao.adapter.driving.restful.orgmng;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import qiyebao.application.orgmng.OrgDto;
import qiyebao.application.orgmng.OrgService;

@RestController
public class OrgController {
    private final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @PostMapping("/api/organizations")
    public OrgDto addOrg(@RequestParam("userid") Long userId
            , @RequestBody OrgDto request) {
        return orgService.addOrg(request, userId);
    }

}
