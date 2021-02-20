package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import vip.testops.manager.entity.dto.CaseDTO;

import java.util.List;

@Mapper
public interface CaseMapper {

    @Select("select count(*) from t_case")
    int queryTotal();

    @Select("select * from t_case")
    List<CaseDTO> getList();

}
