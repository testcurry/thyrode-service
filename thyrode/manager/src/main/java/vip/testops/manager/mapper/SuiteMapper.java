package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.*;
import vip.testops.manager.entity.dto.CaseDTO;
import vip.testops.manager.entity.dto.SuiteDTO;
import vip.testops.manager.entity.vto.SuiteVTO;

import java.util.List;

@Mapper
public interface SuiteMapper {

    @Select("select count(*) from t_suite where status=#{status}")
    int queryByStatus(Integer status);

    @Select("select count(*) from t_suite")
    int total();

    @Select("select * from t_suit where projectId=#{projectId}")
    List<SuiteDTO> getSuiteByProjectId(Long projectId);

    @Select("select caseId,caseName,description,status " +
            "from t_suite inner join t_case using(caseId) where projectId=#{projectId}")
    List<SuiteVTO> getSuiteVTOByProjectId(Long projectId);

}
