package org.arch.oms.rest;

import org.arch.oms.common.request.OrderAddressRequest;
import org.arch.oms.common.vo.OrderAddressVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@RestController
@RequestMapping("orderAddress")
public interface OrderAddressRest  {


    /**
     * 根据 request 查询
     * @param request
     * @return
     */
    @PostMapping("getAddress")
    List<OrderAddressVo> getOrderAddressByRequest(OrderAddressRequest request);




}
