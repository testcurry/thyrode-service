package vip.testops.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.manager.common.Response;
import vip.testops.manager.entity.dto.CaseDTO;
import vip.testops.manager.entity.dto.ProjectDTO;
import vip.testops.manager.entity.dto.SuiteDTO;
import vip.testops.manager.entity.enums.ProjectStatus;
import vip.testops.manager.entity.vto.CaseVTO;
import vip.testops.manager.entity.vto.ProjectVTO;
import vip.testops.manager.entity.vto.SuiteVTO;
import vip.testops.manager.mapper.CaseMapper;
import vip.testops.manager.mapper.ProjectMapper;
import vip.testops.manager.mapper.SuiteMapper;
import vip.testops.manager.service.ProjectService;
import vip.testops.manager.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectMapper projectMapper;
    private CaseMapper caseMapper;
    private SuiteMapper suiteMapper;

    @Autowired
    public void setSuiteMapper(SuiteMapper suiteMapper) {
        this.suiteMapper = suiteMapper;
    }

    @Autowired
    public void setCaseMapper(CaseMapper caseMapper) {
        this.caseMapper = caseMapper;
    }

    @Autowired
    public void setProjectMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Override
    public void doGetList(Response<List<ProjectVTO>> response) {
        List<ProjectVTO> projectVTOlist = new ArrayList<>();
        List<ProjectDTO> projectDTOList = projectMapper.getProjectList();
        projectDTOList.forEach(item -> {
            ProjectVTO projectVTO = new ProjectVTO();
            projectVTO.setProjectId(item.getProjectId());
            projectVTO.setProjectName(item.getProjectName());
            projectVTO.setDescription(item.getDescription());
            //方法一使用switch case 语句：
//            int status=item.getStatus();
//            switch (status){
//                case 0:
//                    projectVTO.setStatus("Ready");
//                    break;
//                case 1:
//                    projectVTO.setStatus("Success");
//                    break;
//                case 2:
//                    projectVTO.setStatus("Fail");
//                    break;
//                case 3:
//                    projectVTO.setStatus("Running");
//                    break;
//                default:
//                    projectVTO.setStatus(null);
//
//            }
            //方法二：利用枚举类
            ProjectStatus status = ProjectStatus.getByKey(item.getStatus());
            projectVTO.setStatus(status == null ? null : status.getValue());
            projectVTOlist.add(projectVTO);
        });
        response.dataSuccess(projectVTOlist);
    }

    @Override
    public void doAddProject(String projectname, String description, Response<?> response) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectName(projectname);
        projectDTO.setDescription(description);
        projectDTO.setStatus(ProjectStatus.READY.getKey());
        int count = projectMapper.addProject(projectDTO);
        if (count != 1) {
            response.serviceError("add project failed");
            return;
        }
        response.commonSuccess();
    }

    @Override
    public void doModifyProject(Long projectId, String projectName, String description, Response<?> response) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(projectId);
        projectDTO.setProjectName(projectName);
        projectDTO.setDescription(description);
        int count = projectMapper.modifyProject(projectDTO);
        if (count != 1) {
            response.serviceError("modify project failed");
            return;
        }
        response.commonSuccess();
    }

    @Override
    public void doRemoveProject(Long projectId, Response<?> response) {
//        if (projectId==null){
//            response.paramMissError("projectId");
//            return;
//        }
        int count = projectMapper.removeProject(projectId);
        if (count != 1) {
            response.serviceError("remove project failed");
            return;
        }
        response.commonSuccess();
    }

    @Override
    public void doGetDetail(Long projectId, Response<ProjectVTO> response) {
        ProjectDTO projectDTO = projectMapper.getProjectById(projectId);
        if (projectDTO == null) {
            response.serviceError("project not exsit");
            return;
        }

        ProjectVTO projectVTO = new ProjectVTO();
        projectVTO.setProjectName(projectDTO.getProjectName());
        projectVTO.setDescription(projectDTO.getDescription());
        int key = projectDTO.getStatus();
        ProjectStatus status = ProjectStatus.getByKey(key);
        projectVTO.setStatus(status == null ? null : status.getValue());

        //方法一：获取project中的所有case详情

       /*
       List<SuiteDTO> suiteDTOList = suiteMapper.getSuiteByProjectId(projectId);
        List<SuiteVTO> suiteVTOList = new ArrayList<>();
        suiteDTOList.forEach(item -> {
            CaseDTO caseDTO = caseMapper.getCaseById(item.getCaseId());
            SuiteVTO suiteVTO = new SuiteVTO();
            suiteVTO.setCaseId(caseDTO.getCaseId());
            suiteVTO.setCaseName(caseDTO.getCaseName());
            suiteVTO.setDescription(caseDTO.getDescription());
            ProjectStatus status1 = ProjectStatus.getByKey(item.getStatus());
            suiteVTO.setStatus(status1==null?null:status1.getValue());
            suiteVTOList.add(suiteVTO);
        });
        projectVTO.setCaseList(suiteVTOList);
        */

        //方法二：获取project中的所有case详情
        List<SuiteVTO> suiteVTOList = suiteMapper.getSuiteVTOByProjectId(projectId);
        projectVTO.setCaseList(suiteVTOList);
        response.dataSuccess(projectVTO);

    }

}
