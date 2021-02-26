package vip.testops.manager.entity.vto;

import lombok.Data;
import vip.testops.manager.entity.dto.CaseDTO;

import java.util.List;

@Data
public class ProjectVTO {
    private Long projectId;
    private String projectName;
    private String description;
    private String status;
    private List<SuiteVTO> caseList;
}
