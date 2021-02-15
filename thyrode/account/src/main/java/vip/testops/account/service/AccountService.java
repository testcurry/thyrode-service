package vip.testops.account.service;

import vip.testops.account.common.Response;
import vip.testops.account.entity.vto.LoginVTO;

public interface AccountService {
    void doLogin(String email, String password, Response<LoginVTO> response);
}
