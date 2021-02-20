package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.manager.entity.dto.HeaderDTO;
import vip.testops.manager.entity.vto.CaseVTO;

import java.util.List;

@Mapper
public interface HeaderMapper {

    @Select("select `headerId`,`name`,`value` from t_header where caseId=#{caseId} ")
    List<HeaderDTO> getHeadersByCaseId(Long caseId);
}
