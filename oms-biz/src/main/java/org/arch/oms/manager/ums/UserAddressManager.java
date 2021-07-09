package org.arch.oms.manager.ums;

import org.arch.framework.beans.Response;
import org.arch.ums.user.req.UserAddressRequest;
import org.arch.ums.user.res.UserAddressResponse;
import org.springframework.stereotype.Component;

/**
 * 用户地址查询
 * @author junboXiang
 * @version V1.0
 * 2021-07-08
 */
@Component
public class UserAddressManager {

//    @Autowired
//    private UserAddressFeignApi userAddressFeignApi;

    /**
     * 通过id 获取用户地址
     * @param id
     * @return
     */
    public UserAddressResponse getUserAddressById(Long id, Long userId) {
        UserAddressRequest userAddressRequest = new UserAddressRequest();
        userAddressRequest.setId(id);
        userAddressRequest.setUserId(userId);
        // fixme 此出放开
//        Response<UserAddressResponse> byId = userAddressFeignApi.findOne(userAddressRequest);
        Response<UserAddressResponse> byId = null;
        return byId.getData();
    }
}
