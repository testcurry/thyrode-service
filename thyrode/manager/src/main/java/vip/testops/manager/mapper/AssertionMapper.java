package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.manager.entity.dto.AssertionDTO;

import java.util.List;

@Mapper
public interface AssertionMapper {

    @Select("select * from t_assertion where caseId=#{caseId}")
    List<AssertionDTO> getAssertionsByCaseId(Long caseId);

    @Insert("INSERT INTO t_assertion VALUES(NULL,#{actual},#{op},#{expected},#{caseId})")
    int addAssertion(AssertionDTO assertionDTO);

    @Delete("delete from t_assertion where caseId=#{caseId}")
    int removeAssertionByCaseId(Long caseId);
}
