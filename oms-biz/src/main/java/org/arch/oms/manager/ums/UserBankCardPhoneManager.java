package org.arch.oms.manager.ums;

import org.arch.framework.beans.Response;
import org.arch.ums.user.res.UserBankCardPhoneRes;
import org.arch.ums.usre.feign.api.UserBankCardPhoneFeignApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 获取用户绑定的支付卡号相关数据
 * @author junboXiang
 * @version V1.0
 * 2021-07-10
 */
@Component
public class UserBankCardPhoneManager {

    @Autowired
    private UserBankCardPhoneFeignApi userBankCardPhoneFeignApi;

    public UserBankCardPhoneRes getUserBankCardPhone(Long id, Long userId, Integer appId) {
        Response<UserBankCardPhoneRes> userBankCardPhoneResResponse = userBankCardPhoneFeignApi.userBankCardPhoneByIdAndUserId(id, userId, appId);
        return userBankCardPhoneResResponse.getData();
    }


}
