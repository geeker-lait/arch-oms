package org.arch.oms.manager;

import org.apache.commons.lang3.StringUtils;
import org.arch.framework.beans.TokenInfo;
import org.arch.framework.beans.utils.TokenInfoUtils;
import org.arch.framework.ums.tenant.context.handler.ArchContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户信息工具类
 * @author junboXiang
 * @version V1.0
 * 2021-06-26
 */
@Component
public class UserHelper {

    @Autowired
    private ArchContextHolder archContextHolder;

    public TokenInfo getTokenInfo() {
        TokenInfo tokenInfo = TokenInfoUtils.getTokenInfo();
        if (tokenInfo == null) {
            tokenInfo = new TokenInfo();
            tokenInfo.setUserId(1L);
            tokenInfo.setAccountId(2L);
            tokenInfo.setAccountName("test");

        }
        return tokenInfo;
    }

    /**
     * 获取当前登录用户id
     * @return
     */
    public Long getUserId() {
        return getTokenInfo().getUserId();
    }

    /**
     * 获取当前登录用户id
     * @return
     */
    public Long getAccountId() {
        return getTokenInfo().getAccountId();
    }

    /**
     * 获取当前登录 用户名
     * @return
     */
    public String getUserName() {
        return getTokenInfo().getAccountName();
    }

    /**
     * 获取appId 为空 给1
     * @return
     */
    public Long getAppId() {
        String appId = archContextHolder.getArchInfo().getAppId();
        if (StringUtils.isEmpty(appId)) {
            return 1L;
        }
        return Long.valueOf(appId);
    }


}
