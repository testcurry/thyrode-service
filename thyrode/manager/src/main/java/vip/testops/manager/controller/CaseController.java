package vip.testops.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vip.testops.manager.common.Response;
import vip.testops.manager.service.CaseService;

@RestController
@RequestMapping("/case")
public class CaseController {

    private CaseService caseService;

    @Autowired
    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping("/total")
    @ResponseBody
    public Response<Integer> total(){
        Response<Integer> response = new Response<>();
        caseService.doQueryTotal(response);
        return response;
    }
}
