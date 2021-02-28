package vip.testops.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.manager.common.Response;
import vip.testops.manager.entity.dto.ProjectDTO;
import vip.testops.manager.entity.request.ProjectAddModifyRequest;
import vip.testops.manager.entity.vto.ProjectVTO;
import vip.testops.manager.service.ProjectService;
import vip.testops.manager.utils.StringUtil;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/list")
    public Response<List<ProjectVTO>> getList() {
        Response<List<ProjectVTO>> response = new Response<>();
        projectService.doGetList(response);
        return response;
    }

    @PostMapping("/add")
    @ResponseBody
    public Response<ProjectVTO> addProject(@RequestBody ProjectAddModifyRequest request) {
        Response<ProjectVTO> response = new Response<>();
        if (StringUtil.isEmptyOrNull(request.getProjectName())) {
            response.paramMissError("projectName");
            return response;
        }
        projectService.doAddProject(request.getProjectName(), request.getDecription(), response);
        return response;

    }

    @PostMapping("/modify")
    @ResponseBody
    public Response<ProjectVTO> modifyProject(@RequestBody ProjectAddModifyRequest request) {
        Response<ProjectVTO> response = new Response<>();
        if (request.getProjectId() == null) {
            response.paramMissError("projectId");
            return response;
        }
        if (StringUtil.isEmptyOrNull(request.getProjectName())) {
            response.paramMissError("projectName");
            return response;
        }
        projectService.doModifyProject(request.getProjectId(), request.getProjectName(), request.getDecription(), response);
        return response;
    }

    @GetMapping("/{id}/remove")
    @ResponseBody
    public Response<ProjectVTO> removeProject(@PathVariable("id") Long projectId) {
        Response<ProjectVTO> response = new Response<>();
        projectService.doRemoveProject(projectId, response);
        return response;
    }

    @GetMapping("/{id}/detail")
    @ResponseBody
    public Response<ProjectVTO> getDetail(@PathVariable("id") Long projectId){
        Response<ProjectVTO> response = new Response<>();
        projectService.doGetDetail(projectId,response);
        return response;
    }

    @GetMapping("/{id}/execute")
    @ResponseBody
    public Response<?> projectExecute(@PathVariable("id") Long projectId){
        Response<?> response = new Response<>();



        return response;
    }

}
