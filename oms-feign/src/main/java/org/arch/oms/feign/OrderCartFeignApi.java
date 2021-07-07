package org.arch.oms.feign;

import org.arch.framework.web.feign.config.DeFaultFeignConfig;
import org.arch.oms.api.OrderCartApi;
import org.arch.oms.common.common.Constant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 购物车操作
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */

@FeignClient(name = Constant.OMS_SERVICE_NAME, contextId = "arch-ums-api-orderCart", path = "/oms/cart",
        configuration = DeFaultFeignConfig.class)
public interface OrderCartFeignApi extends OrderCartApi {

}
