package org.arch.oms.rest;

import org.arch.oms.common.request.OrderSaveRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@RestController
@RequestMapping("orderMaster")
public interface OrderMasterRest {


    /**
     *  根据请求 生成 订单
     * @param request
     * @return
     */
    @PostMapping("/save")
    Boolean save(@RequestBody OrderSaveRequest request);






}
