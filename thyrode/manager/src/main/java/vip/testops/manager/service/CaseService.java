package vip.testops.manager.service;

import vip.testops.manager.common.Response;
import vip.testops.manager.entity.vto.CaseVTO;

import java.util.List;

public interface CaseService {
    void doQueryTotal(Response<Integer> response);

    void doPassPercent(Response<Double> response);

    void doGetList(String key, Response<List<CaseVTO>> response);

    void doAddCase(CaseVTO caseVTO,Response<?>response);
}
