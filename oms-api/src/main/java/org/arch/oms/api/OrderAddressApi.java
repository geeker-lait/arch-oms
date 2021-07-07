package org.arch.oms.api;

import org.arch.oms.common.request.OrderAddressRequest;
import org.arch.oms.common.vo.OrderAddressVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */
public interface OrderAddressApi {

    /**
     * 根据 request 查询
     * @param request
     * @return
     */
    @PostMapping("get")
    List<OrderAddressVo> getOrderAddressByRequest(@RequestBody OrderAddressRequest request);

}
