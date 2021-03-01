package vip.testops.engine.service;


import vip.testops.engine.common.Response;
import vip.testops.engine.entity.vto.CaseVTO;
import vip.testops.engine.entity.vto.ExecResultVTO;

public interface ExecutionService {

    void doExecuteCase(Long projectId, CaseVTO caseVTO, Response<ExecResultVTO> response);
}
