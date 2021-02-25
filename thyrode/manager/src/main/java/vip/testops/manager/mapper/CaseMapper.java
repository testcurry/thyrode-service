package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import vip.testops.manager.entity.dto.CaseDTO;

import java.util.List;

@Mapper
public interface CaseMapper {

    @Select("select count(*) from t_case")
    int queryTotal();

    @Select("select * from t_case")
    List<CaseDTO> getList();

    @Insert("INSERT INTO t_case VALUES(null,#{caseName},#{description}," +
            "#{url},#{method}," +
            "#{body},now(),now())")
    @SelectKey(statement = "select last_insert_id()",keyProperty = "caseId",
            before=false,resultType = Long.class)
    int addCase(CaseDTO caseDTO);

}
