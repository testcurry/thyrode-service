package vip.testops.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.manager.common.Response;
import vip.testops.manager.entity.dto.AssertionDTO;
import vip.testops.manager.entity.dto.CaseDTO;
import vip.testops.manager.entity.dto.HeaderDTO;
import vip.testops.manager.entity.vto.CaseVTO;
import vip.testops.manager.mapper.AssertionMapper;
import vip.testops.manager.mapper.CaseMapper;
import vip.testops.manager.mapper.HeaderMapper;
import vip.testops.manager.mapper.SuiteMapper;
import vip.testops.manager.service.CaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaseServiceImpl implements CaseService {

    private CaseMapper caseMapper;
    private SuiteMapper suiteMapper;
    private HeaderMapper headerMapper;
    private AssertionMapper assertionMapper;

    @Autowired
    public void setHeaderMapper(HeaderMapper headerMapper) {
        this.headerMapper = headerMapper;
    }

    @Autowired
    public void setAssertionMapper(AssertionMapper assertionMapper) {
        this.assertionMapper = assertionMapper;
    }

    @Autowired
    public void setSuiteMapper(SuiteMapper suiteMapper) {
        this.suiteMapper = suiteMapper;
    }

    @Autowired
    public void setCaseMapper(CaseMapper caseMapper) {
        this.caseMapper = caseMapper;
    }

    @Override
    public void doQueryTotal(Response<Integer> response) {
        int total = caseMapper.queryTotal();
        response.dataSuccess(total);
    }

    @Override
    public void doPassPercent(Response<Double> response) {
        int total = suiteMapper.total();
        int pass = suiteMapper.queryByStatus(1);
        double percent = total == 0 ? 0 : pass / (total * 1.0);
        response.dataSuccess(percent*100);
    }

    @Override
    public void doGetList(String key, Response<List<CaseVTO>> response) {
        List<CaseDTO> caseDTOList = caseMapper.getList();
        List<CaseVTO> caseVTOList = new ArrayList<>();
        if (key!=null){
            caseDTOList=caseDTOList.stream().filter((item) ->
            item.getCaseName().contains(key) || item.getDescription().contains(key)
            ).collect(Collectors.toList());
        }
        caseVTOList.forEach((item) ->
        {
            CaseVTO caseVTO = new CaseVTO();
            caseVTO.setCaseId(item.getCaseId());
            caseVTO.setCaseName(item.getCaseName());
            caseVTO.setDescription(item.getDescription());
            caseVTO.setUrl(item.getUrl());
            caseVTO.setMethod(item.getMethod());
            caseVTO.setBody(item.getBody());
            List<HeaderDTO> headerDTOList = headerMapper.getHeadersByCaseId(item.getCaseId());
            List<AssertionDTO> assertionDTOList = assertionMapper.getAssertionsByCaseId(item.getCaseId());
            caseVTO.setHeaders(headerDTOList);
            caseVTO.setAssertions(assertionDTOList);
        });

        response.dataSuccess(caseVTOList);
    }

}
