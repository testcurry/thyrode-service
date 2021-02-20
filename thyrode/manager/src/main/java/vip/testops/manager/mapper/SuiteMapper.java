package vip.testops.manager.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface SuiteMapper {

    @Select("select count(*) from t_suite where status=#{status}")
    int queryByStatus(Integer status);

    @Select("select count(*) from t_suite")
    int total();

}
