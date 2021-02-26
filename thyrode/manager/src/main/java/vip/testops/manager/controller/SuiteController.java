package vip.testops.manager.controller;

import org.springframework.web.bind.annotation.*;
import vip.testops.manager.common.Response;

import java.util.List;

@RestController
@RequestMapping("/suite")
public class SuiteController {

    @PostMapping("/#{projectId}/update")
    @ResponseBody
    public Response<?> updateSuite(
            @PathVariable("projectId") Long projectId,
            @RequestBody List<Long> caseIdList){
        Response<?> response = new Response<>();

        if (caseIdList == null) {
            response.paramMissError("caseList");
            return response;
        }
        return response;


    }
}
