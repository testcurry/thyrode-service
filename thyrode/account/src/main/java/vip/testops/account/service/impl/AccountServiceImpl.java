package vip.testops.account.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.testops.account.common.BaseCodeEnum;
import vip.testops.account.common.Response;
import vip.testops.account.entity.dto.AccountDTO;
import vip.testops.account.entity.vto.LoginVTO;
import vip.testops.account.mapper.AccountMapper;
import vip.testops.account.service.AccountService;
import vip.testops.account.utils.DigestUtil;

import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private AccountMapper accountMapper;

    @Autowired
    public void setAccountMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    public void doLogin(String email, String password, Response<LoginVTO> response) {
        //1、检查email和password
        AccountDTO accountDTO = accountMapper.getUserByEmail(email);
        if (accountDTO == null) {
            response.serviceError("email not valid");
            return;
        }
        try {
            password = DigestUtil.digest(password + accountDTO.getSalt(), "SHA-256");
        } catch (NoSuchAlgorithmException e) {
            response.serviceError("digest error");
            log.error("System error while checking password", e);
            return;
        }
        if (!(password == accountDTO.getPassword())) {
            response.serviceError("password not valid");
            return;
        }

        //2、更新最新登录时间
        accountMapper.updateLastLoginTime(accountDTO.getAccountId());
        //3、生成token

        //4、保存token到缓存Redis中
    }
}
