package vip.testops.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.manager.common.Response;
import vip.testops.manager.mapper.CaseMapper;
import vip.testops.manager.service.CaseService;

@Service
public class CaseServiceImpl implements CaseService {

    private CaseMapper caseMapper;

    @Autowired
    public void setCaseMapper(CaseMapper caseMapper) {
        this.caseMapper = caseMapper;
    }

    @Override
    public void doQueryTotal(Response<Integer> response) {
        int total = caseMapper.queryTotal();
        response.dataSuccess(total);
    }
}
