package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.manager.entity.dto.HeaderDTO;

import java.util.List;

@Mapper
public interface HeaderMapper {

    @Select("select `headerId`,`name`,`value` from t_header where caseId=#{caseId} ")
    List<HeaderDTO> getHeadersByCaseId(Long caseId);

    @Insert("INSERT INTO t_header VALUES(NULL,#{name},#{value},#{caseId})")
    int addHeader(HeaderDTO headerDTO);

    @Delete("delete from t_header where caseId=#{caseId}")
    int removeHeaderByCaseId(Long caseId);
}
