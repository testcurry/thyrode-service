package vip.testops.manager.entity.request;

import lombok.Data;

@Data
public class ProjectAddModifyRequest {

    private Long projectId;
    private String ProjectName;
    private String decription;

}
