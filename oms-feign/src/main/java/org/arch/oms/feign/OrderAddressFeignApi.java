package org.arch.oms.feign;

import org.arch.framework.web.feign.config.DeFaultFeignConfig;
import org.arch.oms.api.OrderAddressApi;
import org.arch.oms.common.common.Constant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */
@FeignClient(name = Constant.OMS_SERVICE_NAME, contextId = "arch-ums-api-orderAddress", path = "/oms/address",
        configuration = DeFaultFeignConfig.class)
public interface OrderAddressFeignApi extends OrderAddressApi {


}
