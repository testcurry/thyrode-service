package vip.testops.account.controller;

import org.springframework.web.bind.annotation.*;
import vip.testops.account.common.Response;
import vip.testops.account.entity.request.LoginRequest;
import vip.testops.account.entity.vto.LoginVTO;
import vip.testops.account.utils.StringUtil;

@RestController
@RequestMapping("/account")
public class AccountController {

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
        return response;
    }
}