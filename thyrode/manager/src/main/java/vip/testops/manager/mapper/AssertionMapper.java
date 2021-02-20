package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.manager.entity.dto.AssertionDTO;
import vip.testops.manager.entity.dto.HeaderDTO;

import java.util.List;

@Mapper
public interface AssertionMapper {

    @Select("select `assertionId`,`actual`,`op`,`expected` from t_assertion where caseId=#{caseId}")
    List<AssertionDTO> getAssertionsByCaseId(Long caseId);
}
