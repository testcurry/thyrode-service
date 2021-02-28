package vip.testops.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.testops.manager.common.Response;
import vip.testops.manager.entity.dto.CaseDTO;
import vip.testops.manager.entity.dto.DetailDTO;
import vip.testops.manager.entity.dto.ProjectDTO;
import vip.testops.manager.entity.dto.SuiteDTO;
import vip.testops.manager.entity.enums.SuiteStatus;
import vip.testops.manager.entity.vto.SuiteVTO;
import vip.testops.manager.mapper.CaseMapper;
import vip.testops.manager.mapper.ProjectMapper;
import vip.testops.manager.mapper.SuiteMapper;
import vip.testops.manager.service.SuiteService;

import java.util.List;

@Service
@Transactional
public class SuiteServiceImpl implements SuiteService {

    private ProjectMapper projectMapper;
    private SuiteMapper suiteMapper;
    private CaseMapper caseMapper;

    @Autowired
    public void setProjectMapper(ProjectMapper projectMapper) {
        this.projectMapper = projectMapper;
    }

    @Autowired
    public void setSuiteMapper(SuiteMapper suiteMapper) {
        this.suiteMapper = suiteMapper;
    }

    @Autowired
    public void setCaseMapper(CaseMapper caseMapper) {
        this.caseMapper = caseMapper;
    }

    @Override
    public void doUpdateSuite(Long projectId, List<Long> caseIdList, Response<?> response) {

        ProjectDTO projectDTO = projectMapper.getProjectById(projectId);
        if (projectDTO == null) {
            response.serviceError("project not available");
            return;
        }
        //将suite表中的关于该projectId的所有记录全部删除
        suiteMapper.removeSuiteByProjectId(projectId);
        //添加新的关联关系
        for (Long caseId : caseIdList) {
            CaseDTO caseDTO = caseMapper.getCaseById(caseId);
            if (caseDTO == null) {
                SuiteDTO suiteDTO = new SuiteDTO();
                suiteDTO.setProjectId(projectId);
                suiteDTO.setCaseId(caseId);
                suiteDTO.setDuration(-1L);
                suiteDTO.setStatus(SuiteStatus.Initial.getKey());
                suiteMapper.addSuite(suiteDTO);
            }
        }
        response.commonSuccess();
    }
}
