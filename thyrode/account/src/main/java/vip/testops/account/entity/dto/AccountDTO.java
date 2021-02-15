package vip.testops.account.entity.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private long accountId;
    private String accountName;
    private String email;
    private String salt;
    private String password;
    private String createTime;
    private String lastLoginTime;
}
