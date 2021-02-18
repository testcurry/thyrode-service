package vip.testops.manager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vip.testops.manager.common.Response;
import vip.testops.manager.entity.vto.BugVTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bug")
public class BugController {

    @GetMapping("/overtime")
    @ResponseBody
    public Response<List<BugVTO>> getOverTime() {
        ArrayList<BugVTO> bugVTOList = new ArrayList<>();
        bugVTOList.add(new BugVTO("2021-02-18", 12));
        bugVTOList.add(new BugVTO("2021-02-19", 21));
        bugVTOList.add(new BugVTO("2021-02-20", 22));
        bugVTOList.add(new BugVTO("2021-02-21", 23));
        bugVTOList.add(new BugVTO("2021-02-22", 24));
        Response<List<BugVTO>> response=new Response<>();
        response.dataSuccess(bugVTOList);
        return response;
    }

}
