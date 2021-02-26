package vip.testops.manager.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
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

    @Override
    public void doAddCase(CaseVTO caseVTO, Response<?> response) {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseName(caseVTO.getCaseName());
        caseDTO.setDescription(caseVTO.getDescription());
        caseDTO.setUrl(caseVTO.getUrl());
        caseDTO.setMethod(caseVTO.getBody());
        if (caseMapper.addCase(caseDTO)!=1){
            response.serviceError("add case failed");
            return;
        }
        List<HeaderDTO> headers = caseVTO.getHeaders();
        //遍历给列表中的每一个header赋值
        //方法一
        headers.forEach(header -> {
            HeaderDTO headerDTO = new HeaderDTO();
            headerDTO.setName(header.getName());
            headerDTO.setValue(header.getValue());
            headerDTO.setCaseId(header.getCaseId());
            int count = headerMapper.addHeader(headerDTO);
            if (count != 1) {
                log.warn("add header {} to database failed",headerDTO);
            }
        });
        //方法二
       /* Iterator<HeaderDTO> iterator = headers.iterator();
        if (iterator.hasNext()) {
            HeaderDTO header = iterator.next();
            HeaderDTO headerDTO = new HeaderDTO();
            headerDTO.setName(header.getName());
            headerDTO.setValue(header.getValue());
            headerDTO.setCaseId(header.getCaseId());
            headerMapper.addHeader(headerDTO);
        }*/

        //遍历给列表中的每一个case中的assertion赋值
        List<AssertionDTO> assertions = caseVTO.getAssertions();
        assertions.forEach(assertion->{
            AssertionDTO assertionDTO = new AssertionDTO();
            assertionDTO.setCaseId(caseDTO.getCaseId());
            assertionDTO.setActual(assertion.getActual());
            assertionDTO.setOp(assertion.getOp());
            assertionDTO.setExpected(assertion.getExpected());
            int count = assertionMapper.addAssertion(assertionDTO);
            if (count != 1) {
                log.warn("add assertion {} to database failed",assertionDTO);
            }
        });
        response.commonSuccess();
    }

    @Override
    public void doModifyCase(CaseVTO caseVTO, Response<?> response) {
        CaseDTO caseDTO = new CaseDTO();
        caseDTO.setCaseId(caseVTO.getCaseId());
        caseDTO.setCaseName(caseVTO.getCaseName());
        caseDTO.setDescription(caseVTO.getDescription());
        caseDTO.setUrl(caseVTO.getUrl());
        caseDTO.setMethod(caseVTO.getMethod());
        caseDTO.setBody(caseVTO.getBody());
        int count = caseMapper.modifyCase(caseDTO);
        if (count != 1) {
            response.serviceError("modify case failed");
            return;
        }
        headerMapper.removeHeaderByCaseId(caseDTO.getCaseId());
        caseVTO.getHeaders().forEach(header -> {
            HeaderDTO headerDTO = new HeaderDTO();
            headerDTO.setHeaderId(header.getHeaderId());
            headerDTO.setName(header.getName());
            headerDTO.setValue(header.getValue());
            headerDTO.setCaseId(caseVTO.getCaseId());
            if (headerMapper.addHeader(headerDTO)!=1){
                response.serviceError("add hearder failed");
                return;
            }
        });
        assertionMapper.removeAssertionByCaseId(caseDTO.getCaseId());
        caseVTO.getAssertions().forEach(assertion ->{
            AssertionDTO assertionDTO = new AssertionDTO();
            assertionDTO.setAssertionId(assertion.getAssertionId());
            assertionDTO.setOp(assertion.getOp());
            assertionDTO.setExpected(assertion.getExpected());
            assertionDTO.setActual(assertion.getActual());
            assertionDTO.setCaseId(assertion.getCaseId());
            if (assertionMapper.addAssertion(assertionDTO)!=1){
                response.serviceError("add hearder failed");
                return;
            }
            response.commonSuccess();
        });

    }

    @Override
    public void doRemoveCase(Long caseId, Response<?> response) {
        int count = caseMapper.removeCaseByCaseId(caseId);
        if (count != 1) {
            response.serviceError("delete case failed");
            return;
        }
        response.commonSuccess();

        //删除该case所关联的header表中的记录和assertion表中的记录
        headerMapper.removeHeaderByCaseId(caseId);
        assertionMapper.removeAssertionByCaseId(caseId);
        response.commonSuccess();

    }

}
