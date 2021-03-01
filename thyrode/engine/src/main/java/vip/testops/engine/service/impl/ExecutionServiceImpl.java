package vip.testops.engine.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.testops.engine.common.Response;
import vip.testops.engine.entity.vto.CaseVTO;
import vip.testops.engine.entity.vto.ExecResultVTO;
import vip.testops.engine.service.ExecutionService;

@Service
@Slf4j
public class ExecutionServiceImpl implements ExecutionService {
    @Override
    public void doExecuteCase(Long projectId, CaseVTO caseVTO, Response<ExecResultVTO> response) {


    }
}
