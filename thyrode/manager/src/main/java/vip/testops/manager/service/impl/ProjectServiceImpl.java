package vip.testops.manager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.manager.common.Response;
import vip.testops.manager.entity.dto.*;
import vip.testops.manager.entity.enums.ProjectStatus;
import vip.testops.manager.entity.enums.SuiteStatus;
import vip.testops.manager.entity.vto.CaseVTO;
import vip.testops.manager.entity.vto.ExecutionVTO;
import vip.testops.manager.entity.vto.ProjectVTO;
import vip.testops.manager.entity.vto.SuiteVTO;
import vip.testops.manager.mapper.*;
import vip.testops.manager.mq.Sender;
import vip.testops.manager.service.ProjectService;
import vip.testops.manager.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private ProjectMapper projectMapper;
    private CaseMapper caseMapper;
    private SuiteMapper suiteMapper;
    private HeaderMapper headerMapper;
    private AssertionMapper assertionMapper;
    private Sender sender;

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

    @Autowired
    public void setHeaderMapper(HeaderMapper headerMapper) {
        this.headerMapper = headerMapper;
    }

    @Autowired
    public void setAssertionMapper(AssertionMapper assertionMapper) {
        this.assertionMapper = assertionMapper;
    }

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
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
        List<DetailDTO> detailDTOList = suiteMapper.getSuiteVTOByProjectId(projectId);
        List<SuiteVTO> suiteVTOList = new ArrayList<>();
        detailDTOList.forEach(item -> {
            SuiteVTO suiteVTO = new SuiteVTO();
            suiteVTO.setCaseId(item.getCaseId());
            suiteVTO.setCaseName(item.getCaseName());
            suiteVTO.setDescription(item.getDescription());
            Integer statusKey = item.getStatus();
            SuiteStatus suiteStatus = SuiteStatus.getByKey(statusKey);
            suiteVTO.setStatus(suiteStatus==null?null:suiteStatus.getValue());
            suiteVTOList.add(suiteVTO);
        });
        projectVTO.setCaseList(suiteVTOList);
        response.dataSuccess(projectVTO);

    }

    @Override
    public void doExecuteProject(Long projectId, Response<?> response) {
        //根据projectId获取t_project数据表中的实体
        ProjectDTO projectDTO = projectMapper.getProjectById(projectId);
        if (projectDTO == null) {
            response.serviceError("project not available");
            return;
        }

        //构造需要发送到消息对列的消息体
        ExecutionVTO executionVTO = new ExecutionVTO();
        executionVTO.setProjectId(projectId);
        executionVTO.setProjectName(projectDTO.getProjectName());
       List<CaseVTO> caseVTOList = new ArrayList<>();
        List<SuiteDTO> suiteVTOList = suiteMapper.getSuiteByProjectId(projectId);
        suiteVTOList.forEach(item ->{
            CaseDTO caseDTO = caseMapper.getCaseById(item.getCaseId());
            CaseVTO caseVTO = new CaseVTO();
            caseVTO.setCaseId(caseDTO.getCaseId());
            caseVTO.setCaseName(caseDTO.getCaseName());
            caseVTO.setUrl(caseDTO.getUrl());
            caseVTO.setMethod(caseDTO.getMethod());
            caseVTO.setDescription(caseDTO.getDescription());
            caseVTO.setBody(caseDTO.getBody());

            //给每条case的header和assertion赋值
            List<HeaderDTO> headerList = headerMapper.getHeadersByCaseId(caseDTO.getCaseId());
            List<AssertionDTO> assertionDTOList = assertionMapper.getAssertionsByCaseId(caseDTO.getCaseId());
            caseVTO.setHeaders(headerList);
            caseVTO.setAssertions(assertionDTOList);
            caseVTOList.add(caseVTO);
        });
        executionVTO.setCaseList(caseVTOList);

        //发送消息体给消息队列rabbitmq...
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = objectMapper.writeValueAsString(executionVTO);
            sender.send(message);
        } catch (JsonProcessingException e) {
            log.error("object to json failed",e);
            response.serviceError("object to json failed");
        }

        //将project的状态置为执行中running
        projectMapper.updateProjectStatusById(projectId,ProjectStatus.RUNNING.getKey());

        //将suite表中的所有关联的用例状态重新置为initial
        suiteMapper.updateSuiteStatusById(projectId, SuiteStatus.Initial.getKey());

        response.commonSuccess();


    }

}
