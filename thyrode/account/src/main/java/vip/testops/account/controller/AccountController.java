package vip.testops.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.testops.account.common.Response;
import vip.testops.account.entity.request.LoginRequest;
import vip.testops.account.entity.vto.AccountVTO;
import vip.testops.account.entity.vto.LoginVTO;
import vip.testops.account.service.AccountService;
import vip.testops.account.utils.StringUtil;

@RestController
@RequestMapping("/account")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    @ResponseBody
    public Response<LoginVTO> login(@RequestBody LoginRequest loginRequest){
        Response<LoginVTO> response = new Response<>();
        //参数检查
        if (StringUtil.isEmptyOrNull(loginRequest.getEmail())){
            response.paramMissError("email");
            return response;
        }
        if (StringUtil.isEmptyOrNull(loginRequest.getPassword())){
            response.paramMissError("password");
            return response;
        }

        //进入服务层处理
        accountService.doLogin(loginRequest.getEmail(),loginRequest.getPassword(),response);
        return response;
    }

    @GetMapping("/authorize")
    @ResponseBody
    public Response<AccountVTO> authorize(
            @RequestParam(value = "token", required = false) String token
    ){
        Response<AccountVTO> response = new Response<>();
        // 参数校验
        if(StringUtil.isEmptyOrNull(token)){
            response.paramMissError("token");
            return response;
        }
        // 进入服务层处理
        accountService.doAuthorize(token, response);
        return response;
    }
}
