package org.arch.oms.manager.ums;

import org.arch.framework.beans.Response;
import org.arch.ums.user.req.UserAddressRequest;
import org.arch.ums.user.res.UserAddressResponse;
import org.arch.ums.usre.feign.api.UserAddressFeignApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户地址查询
 * @author junboXiang
 * @version V1.0
 * 2021-07-08
 */
@Component
public class UserAddressManager {

    @Autowired
    private UserAddressFeignApi userAddressFeignApi;
//
    /**
     * 通过id 获取用户地址
     * @param id
     * @return
     */
    public UserAddressResponse getUserAddressById(Long id, Long userId) {
        UserAddressRequest userAddressRequest = new UserAddressRequest();
        userAddressRequest.setId(id);
        userAddressRequest.setUserId(userId);
        Response<UserAddressResponse> byId = userAddressFeignApi.findOne(userAddressRequest);
        return byId.getData();
    }
}
