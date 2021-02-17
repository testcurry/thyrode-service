package vip.testops.account.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import vip.testops.account.common.Response;
import vip.testops.account.entity.dto.AccountDTO;
import vip.testops.account.entity.vto.AccountVTO;
import vip.testops.account.entity.vto.LoginVTO;
import vip.testops.account.mapper.AccountMapper;
import vip.testops.account.service.AccountService;
import vip.testops.account.utils.DigestUtil;
import vip.testops.account.utils.JWTUtil;
import vip.testops.account.utils.StringUtil;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Value("${jwt.expire}")
    private int tokenExpire;
    @Value("${jwt.key}")
    private String jwtKey;
    private AccountMapper accountMapper;
    private StringRedisTemplate redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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
        if (!password.equals(accountDTO.getPassword())){
            response.serviceError("password not valid");
            return;
        }

        //2、更新最新登录时间
        int col = accountMapper.updateLastLoginTime(accountDTO.getAccountId());
        if (col == 0) {
            response.serviceError("update login time error");
            return;
        }
        //3、生成token
        HashMap<String, Object> claim = new HashMap<>();
        claim.put("id", accountDTO.getAccountId());
        claim.put("username", accountDTO.getAccountName());
        claim.put("email", accountDTO.getEmail());
        String secretKey = JWTUtil.getSecret(jwtKey);
        String token = JWTUtil.createToken(claim, secretKey, 30);

        //4、保存token到缓存Redis中
        redisTemplate.opsForValue().set(token, secretKey, tokenExpire, TimeUnit.MINUTES);
        LoginVTO loginVTO = new LoginVTO();
        loginVTO.setToken(token);
        loginVTO.setUsername(accountDTO.getAccountName());
        response.dataSuccess(loginVTO);
    }

    @Override
    public void doAuthorize(String token, Response<AccountVTO> response) {

        String secretKey = redisTemplate.opsForValue().get(token);
        if (StringUtil.isEmptyOrNull(secretKey)) {
            response.serviceError("token is invalid");
            return;
        }
        log.info("get secret key from redis: {}", secretKey);
        Claim claim = JWTUtil.verifyToken(token, secretKey);
        AccountVTO accountVTO = new AccountVTO();
        accountVTO.setAccountId(Long.valueOf(claim.asMap().get("id").toString()));
        accountVTO.setAccountName(claim.asMap().get("username").toString());
        accountVTO.setEmail(claim.asMap().get("email").toString());
        // 刷新redis中token过期时间
        redisTemplate.expire(token, tokenExpire, TimeUnit.MINUTES);
        response.dataSuccess(accountVTO);
    }
}
