package org.arch.oms.feign;

import org.arch.framework.web.feign.config.DeFaultFeignConfig;
import org.arch.oms.api.OrderMasterApi;
import org.arch.oms.common.common.Constant;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */
@FeignClient(name = Constant.OMS_SERVICE_NAME, contextId = "arch-ums-api-orderMaster", path = "/oms/order", configuration = DeFaultFeignConfig.class)
public interface OrderMasterFeignApi extends OrderMasterApi {

}
