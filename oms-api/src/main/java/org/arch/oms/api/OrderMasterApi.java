package org.arch.oms.api;

import org.arch.framework.beans.Response;
import org.arch.oms.common.request.OrderInfoManagerQueryRequest;
import org.arch.oms.common.request.OrderInfoQueryRequest;
import org.arch.oms.common.request.OrderInfoUserQueryRequest;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.common.request.PayOrderByCardRequest;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.PageVo;
import org.springframework.web.bind.annotation.GetMapping;
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
    Response<OrderInfoVo> save(@RequestBody OrderSaveRequest request);

    /**
     * 根据订单号查询订单
     * @param request
     * @return
     */
    @PostMapping("/orderDetail")
    Response<OrderInfoVo> queryOrder(@RequestBody OrderInfoQueryRequest request);

    /**
     * 根据订单支付
     * @param request
     * @return
     */
    @GetMapping("/payOrderByCard/{id}")
    Response<Boolean> payOrderByCard(@RequestBody PayOrderByCardRequest request);

    /**
     * 用户 分页查询 订单
     * @param request
     * @return
     */
    @PostMapping("/queryOrderPageList")
    Response<PageVo<List<OrderInfoVo>>> queryOrderPageList(@RequestBody OrderInfoUserQueryRequest request);

    /**
     * 根据订单号查询订单
     * @param request
     * @return
     */
    @PostMapping("/manager/orderList")
    Response<List<OrderInfoVo>> queryListOrderByManager(@RequestBody OrderInfoManagerQueryRequest request);

    /**
     * 管理端 分页查询订单
     * @param request
     * @return
     */
    @PostMapping("/manager/queryOrderPageList")
    Response<PageVo<List<OrderInfoVo>>> queryOrderPageListByManager(@RequestBody OrderInfoManagerQueryRequest request);



}
