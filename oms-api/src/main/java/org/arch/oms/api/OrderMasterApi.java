package org.arch.oms.api;

import org.arch.oms.common.request.OrderInfoManagerQueryRequest;
import org.arch.oms.common.request.OrderInfoQueryRequest;
import org.arch.oms.common.request.OrderInfoUserQueryRequest;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.PageVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */
public interface OrderMasterApi {

    /**
     *  根据请求 生成 订单
     * @param request
     * @return
     */
    @PostMapping("/save")
    Boolean save(@RequestBody OrderSaveRequest request);

    /**
     * 根据订单号查询订单
     * @param request
     * @return
     */
    @PostMapping("/orderList")
    List<OrderInfoVo> queryListOrder(@RequestBody OrderInfoQueryRequest request);

    /**
     * 用户 分页查询 订单
     * @param request
     * @return
     */
    @PostMapping("/queryOrderPageList")
    PageVo<List<OrderInfoVo>> queryOrderPageList(@RequestBody OrderInfoUserQueryRequest request);

    /**
     * 管理端 分页查询订单
     * @param request
     * @return
     */
    @PostMapping("/queryOrderPageListByManager")
    PageVo<List<OrderInfoVo>> queryOrderPageListByManager(@RequestBody OrderInfoManagerQueryRequest request);



}
