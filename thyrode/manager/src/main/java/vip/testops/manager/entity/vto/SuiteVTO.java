package vip.testops.manager.entity.vto;

import lombok.Data;

@Data
public class SuiteVTO {
    private Long caseId;
    private String caseName;
    private String description;
    private String status;
}
