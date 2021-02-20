package vip.testops.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.manager.common.Response;
import vip.testops.manager.entity.vto.CaseVTO;
import vip.testops.manager.entity.vto.CoverageVTO;
import vip.testops.manager.service.CaseService;

import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/pass")
    @ResponseBody
    public Response<Double> pass(){
        Response<Double> response = new Response<>();
        caseService.doPassPercent(response);
        return response;
    }

    @GetMapping("coverage_overtime")
    @ResponseBody
    public Response<List<CoverageVTO>> coverage(){
        //并沒有真正从第三方需求或API管理系统中获取覆盖率统计数据，这里仅做演示
        List<CoverageVTO> coverageVTOList=new ArrayList<>();
        coverageVTOList.add(new CoverageVTO("2021-02-19",80.12));
        coverageVTOList.add(new CoverageVTO("2021-02-19",85.34));
        coverageVTOList.add(new CoverageVTO("2021-02-19",84.56));
        coverageVTOList.add(new CoverageVTO("2021-02-19",83.21));
        coverageVTOList.add(new CoverageVTO("2021-02-19",90.23));
        Response<List<CoverageVTO>> response=new Response<>();
        response.dataSuccess(coverageVTOList);
        return response;
    }

    @GetMapping("/list")
    @ResponseBody
    public Response<List<CaseVTO>> getList(@RequestParam(value = "key",required = false) String key){
        Response<List<CaseVTO>> response = new Response<>();
        caseService.doGetList(key,response);
        return response;
    }
}
