package vip.testops.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import vip.testops.account.entity.dto.AccountDTO;

@Mapper
public interface AccountMapper {

    @Select("select * from t_account where email=#{email}")
    AccountDTO getUserByEmail(String email);

    @Update("update t_account set lastLoginTime=NOW() where accountId=#{accountId}")
    int updateLastLoginTime(Long accountId);
}
