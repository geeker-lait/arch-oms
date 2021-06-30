package org.arch.oms.manager;

import org.arch.framework.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户信息工具类
 * @author junboXiang
 * @version V1.0
 * 2021-06-26
 */
@Component
public class UserHelper {

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 获取当前登录用户id
     * @return
     */
    public Long getUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    /**
     * 获取当前登录 用户名
     * @return
     */
    public String getUserName() {
        return SecurityUtils.getAccountName();
    }

    /**
     * 获取appId
     * @return
     */
    public Long getAppId() {
        return -1L;
    }


}
