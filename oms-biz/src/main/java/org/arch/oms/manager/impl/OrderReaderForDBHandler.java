package org.arch.oms.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.ContainerConstants;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.enums.OrderInfoQueryType;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.common.request.OrderInfoQueryRequest;
import org.arch.oms.common.request.OrderSectionRequest;
import org.arch.oms.common.vo.OrderAddressVo;
import org.arch.oms.common.vo.OrderFulfilVo;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.OrderInvoiceVo;
import org.arch.oms.common.vo.OrderItemRelishVo;
import org.arch.oms.common.vo.OrderPaymentVo;
import org.arch.oms.entity.OrderAddress;
import org.arch.oms.entity.OrderFulfil;
import org.arch.oms.entity.OrderInvoice;
import org.arch.oms.entity.OrderItemRelish;
import org.arch.oms.entity.OrderMaster;
import org.arch.oms.entity.OrderPayment;
import org.arch.oms.manager.OrderDetailHandler;
import org.arch.oms.manager.OrderReaderHandler;
import org.arch.oms.service.OrderAddressService;
import org.arch.oms.service.OrderFulfilService;
import org.arch.oms.service.OrderInvoiceService;
import org.arch.oms.service.OrderItemRelishService;
import org.arch.oms.service.OrderMasterService;
import org.arch.oms.service.OrderPaymentService;
import org.arch.oms.utils.BeanCopyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 订单查询处理类 聚合各种信息
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
@Slf4j
@Component
public class OrderReaderForDBHandler extends OrderReaderHandler implements InitializingBean {

    /**
     * 存放构建 订单数据Consumer
     */
    private static final List<BiConsumer<List<OrderInfoVo>, OrderSectionRequest>> buildOtherInfoConsumer = Lists.newArrayList();

    @Autowired
    private OrderMasterService orderMasterService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
     private OrderFulfilService orderFulfilService;
    @Autowired
    private OrderInvoiceService orderInvoiceService;
    @Autowired
    private OrderItemRelishService orderItemRelishService;
    @Autowired
    private OrderPaymentService orderPaymentService;

    @Override
    public OrderInfoVo queryOrder(Long userId, Long orderNo, OrderSectionRequest orderSectionRequest) {
        LambdaQueryWrapper<OrderMaster> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderMaster::getBuyerAccountId, userId).eq(OrderMaster::getOrderNo, orderNo);
        OrderMaster oneBySpec = orderMasterService.findOneBySpec(queryWrapper);
        if (oneBySpec == null) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("订单不存在"));
        }
        List<OrderInfoVo> orderInfoVos = queryBuildOrderInfo(Lists.newArrayList(oneBySpec), orderSectionRequest);
        return orderInfoVos.get(0);
    }

    @Override
    public List<OrderInfoVo> queryOrderList(OrderInfoQueryRequest request) {
        // todo 订单查询实现 查询出具体的订单数据
        List<OrderMaster> orderMasters = Lists.newArrayList();

        return queryBuildOrderInfo(orderMasters, request.getOrderSection());
    }

    @Override
    public List<OrderInfoVo> queryOrderByManager(OrderInfoQueryRequest request) {
        // todo 订单查询实现 查询出具体的订单数据
        List<OrderMaster> orderMasters = Lists.newArrayList();


        return queryBuildOrderInfo(orderMasters, request.getOrderSection());
    }

    /**
     * 根据 orderMsster 填充 其他数据
     * @param orderMasters
     * @param orderSectionRequest
     * @return
     */
    private List<OrderInfoVo> queryBuildOrderInfo(List<OrderMaster> orderMasters, OrderSectionRequest orderSectionRequest) {
        List<OrderInfoVo> orderInfoVos = orderMasterToOrderInfoVo(orderMasters);
        if (orderSectionRequest == null || !needBuildInfoDetail(orderSectionRequest)) {
            return orderInfoVos;
        }
        buildOtherInfoConsumer.stream().forEach(execute -> execute.accept(orderInfoVos, orderSectionRequest));
        return orderInfoVos;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ORDER_READER_HANDLER_MAP.put(OrderInfoQueryType.DB.getValue(), this);
        buildOrderAddressFunction();
        buildOrderFulfilFunction();
        buildOrderInvoiceFunction();
        buildOrderItemRelishFunction();
        buildOrderPaymentFunction();
        buildOrderDetailFunction();

    }


    private void buildOrderAddressFunction() {
        // address 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderAddressServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderAddress()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getOrderNo(), orderInfoVo -> orderInfoVo));
            LambdaQueryWrapper<OrderAddress> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(OrderAddress::getOrderNo, collect.keySet());
            List<OrderAddress> allBySpec = orderAddressService.findAllBySpec(queryWrapper);
            Map<Long, List<OrderAddress>> orderNoRelevancy = allBySpec.stream().collect(Collectors.groupingBy(orderAddress -> orderAddress.getOrderNo(), Collectors.toList()));
            orderNoRelevancy.entrySet().forEach(entrySet -> {
                if (collect.containsKey(entrySet.getKey())) {
                    collect.get(entrySet.getKey()).setOrderAddressVoList(BeanCopyUtil.convert(entrySet.getValue(), OrderAddressVo.class));
                }
            });
        };
        buildOtherInfoConsumer.add(orderAddressServiceConsumer);
    }

    private void buildOrderFulfilFunction() {
        // fulfil 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderFulfilServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderFulfil()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getOrderNo(), orderInfoVo -> orderInfoVo));
            LambdaQueryWrapper<OrderFulfil> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(OrderFulfil::getOrderNo, collect.keySet());
            List<OrderFulfil> allBySpec = orderFulfilService.findAllBySpec(queryWrapper);
            Map<Long, List<OrderFulfil>> orderNoRelevancy = allBySpec.stream().collect(Collectors.groupingBy(orderFulfil -> orderFulfil.getOrderNo(), Collectors.toList()));
            orderNoRelevancy.entrySet().forEach(entrySet -> {
                if (collect.containsKey(entrySet.getKey()) && ObjectUtils.isNotEmpty(entrySet.getValue())) {
                    collect.get(entrySet.getKey()).setOrderFulfilVo(BeanCopyUtil.convert(entrySet.getValue().get(0), OrderFulfilVo.class));
                }
            });
        };
        buildOtherInfoConsumer.add(orderFulfilServiceConsumer);
    }

    private void buildOrderInvoiceFunction() {
        // invoice 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderInvoiceServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderInvoice()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getOrderNo(), orderInfoVo -> orderInfoVo));
            LambdaQueryWrapper<OrderInvoice> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(OrderInvoice::getOrderNo, collect.keySet());
            List<OrderInvoice> allBySpec = orderInvoiceService.findAllBySpec(queryWrapper);
            Map<Long, List<OrderInvoice>> orderNoRelevancy = allBySpec.stream().collect(Collectors.groupingBy(orderInvoice -> orderInvoice.getOrderNo(), Collectors.toList()));
            orderNoRelevancy.entrySet().forEach(entrySet -> {
                if (collect.containsKey(entrySet.getKey()) && ObjectUtils.isNotEmpty(entrySet.getValue())) {
                    collect.get(entrySet.getKey()).setOrderInvoiceVo(BeanCopyUtil.convert(entrySet.getValue().get(0), OrderInvoiceVo.class));
                }
            });
        };
        buildOtherInfoConsumer.add(orderInvoiceServiceConsumer);
    }

    private void buildOrderItemRelishFunction() {
        // orderItemRelish 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderItemRelishServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderItemRelish()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getOrderNo(), orderInfoVo -> orderInfoVo));
            LambdaQueryWrapper<OrderItemRelish> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(OrderItemRelish::getOrderNo, collect.keySet());
            List<OrderItemRelish> allBySpec = orderItemRelishService.findAllBySpec(queryWrapper);
            Map<Long, List<OrderItemRelish>> orderNoRelevancy = allBySpec.stream().collect(Collectors.groupingBy(orderInvoice -> orderInvoice.getOrderNo(), Collectors.toList()));
            orderNoRelevancy.entrySet().forEach(entrySet -> {
                if (collect.containsKey(entrySet.getKey()) && ObjectUtils.isNotEmpty(entrySet.getValue())) {
                    collect.get(entrySet.getKey()).setOrderItemRelishVo(BeanCopyUtil.convert(entrySet.getValue().get(0), OrderItemRelishVo.class));
                }
            });
        };
        buildOtherInfoConsumer.add(orderItemRelishServiceConsumer);
    }

    private void buildOrderPaymentFunction() {
        // orderItemRelish 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderPaymentServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderPayment()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getOrderNo(), orderInfoVo -> orderInfoVo));
            LambdaQueryWrapper<OrderPayment> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(OrderPayment::getOrderNo, collect.keySet());
            List<OrderPayment> allBySpec = orderPaymentService.findAllBySpec(queryWrapper);
            Map<Long, List<OrderPayment>> orderNoRelevancy = allBySpec.stream().collect(Collectors.groupingBy(orderInvoice -> orderInvoice.getOrderNo(), Collectors.toList()));
            orderNoRelevancy.entrySet().forEach(entrySet -> {
                if (collect.containsKey(entrySet.getKey()) && ObjectUtils.isNotEmpty(entrySet.getValue())) {
                    collect.get(entrySet.getKey()).setOrderPaymentVo(BeanCopyUtil.convert(entrySet.getValue().get(0), OrderPaymentVo.class));
                }
            });
        };
        buildOtherInfoConsumer.add(orderPaymentServiceConsumer);
    }

    private void buildOrderDetailFunction() {
        // orderItem 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderItemServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderItem()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getOrderNo(), orderInfoVo -> orderInfoVo));
            Map<Long, List<Object>> orderItemMap = ContainerConstants.ORDER_ITEM_QUERY.get(OrderItemTable.PERSONAL.getValue()).queryByOrderNoList(Lists.newArrayList(collect.keySet()));
            if (ObjectUtils.isEmpty(orderItemMap)) {
                return;
            }
            orderItemMap.entrySet().forEach(orderDetailEntrySet -> {
                Long orderNo = orderDetailEntrySet.getKey();
                if (collect.containsKey(orderNo)) {
                    collect.get(orderNo).setOrderDetailVo(OrderDetailHandler.getHandler(OrderItemTable.PERSONAL.getValue()).convertVo(orderDetailEntrySet.getValue()));

                }
            });

        };
        buildOtherInfoConsumer.add(orderItemServiceConsumer);
    }


}
