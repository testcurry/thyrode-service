package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CaseMapper {

    @Select("select count(*) from t_case")
    int queryTotal();
}
