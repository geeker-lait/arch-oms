package org.arch.oms.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.arch.oms.common.ContainerConstants;
import org.arch.oms.common.dto.OrderInfoSearchDto;
import org.arch.oms.common.enums.OrderInfoQueryType;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.common.request.OrderSectionRequest;
import org.arch.oms.common.request.PageInfo;
import org.arch.oms.common.vo.OrderAddressVo;
import org.arch.oms.common.vo.OrderFulfilVo;
import org.arch.oms.common.vo.OrderInfoVo;
import org.arch.oms.common.vo.OrderInvoiceVo;
import org.arch.oms.common.vo.OrderItemRelishVo;
import org.arch.oms.common.vo.OrderPaymentVo;
import org.arch.oms.common.vo.PageVo;
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
 * 订单查询处理类 聚合各种信息 - DB
 * @author junboXiang
 * @version V1.0
 * 2021-06-30
 */
@Slf4j
@Component
public class OrderReaderForDBHandler extends OrderReaderHandler implements InitializingBean {

    /**
     * 存放构建 订单数据Consumer map 不同的表数据不同的 consumer 实现
     */
    private static final List<BiConsumer<List<OrderInfoVo>, OrderSectionRequest>> buildOrderOtherInfoConsumer = Lists.newArrayList();

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
    public List<OrderInfoVo> queryListOrder(Long userId, List<Long> orderIds, OrderSectionRequest orderSectionRequest) {
        LambdaQueryWrapper<OrderMaster> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderMaster::getBuyerAccountId, userId).in(OrderMaster::getId, orderIds);
        List<OrderMaster> oneBySpec = orderMasterService.findAllBySpec(queryWrapper);
        if (ObjectUtils.isEmpty(oneBySpec)) {
            return Lists.newArrayList();
        }
        return queryBuildOrderInfo(oneBySpec, orderSectionRequest);
    }

    @Override
    public PageVo<List<OrderInfoVo>> queryOrderPageList(OrderInfoSearchDto request) {
        PageInfo pageInfo = request.getPageInfo() == null ? new PageInfo().checkPageInfo() : request.getPageInfo().checkPageInfo();
        LambdaQueryWrapper<OrderMaster> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderMaster::getBuyerAccountId, request.getUserId()).eq(OrderMaster::getAppId, request.getAppId());
        if (request.getStoreNo() != null) {
            queryWrapper.eq(OrderMaster::getStoreNo, request.getStoreNo());
        }
        if (ObjectUtils.isNotEmpty(request.getOrderNoList())) {
            queryWrapper.in(OrderMaster::getId, request.getOrderNoList());
        }
        return buildByPage(pageInfo, queryWrapper, request);
    }

    @Override
    public PageVo<List<OrderInfoVo>> queryOrderPageListByManager(OrderInfoSearchDto request) {
        PageInfo pageInfo = request.getPageInfo() == null ? new PageInfo().checkPageInfo() : request.getPageInfo().checkPageInfo();
        LambdaQueryWrapper<OrderMaster> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderMaster::getStoreNo, request.getStoreNo()).eq(OrderMaster::getAppId, request.getAppId());
        if (request.getUserId() != null) {
            queryWrapper.eq(OrderMaster::getBuyerAccountId, request.getUserId());
        }
        if (ObjectUtils.isNotEmpty(request.getOrderNoList())) {
            queryWrapper.in(OrderMaster::getId, request.getOrderNoList());
        }
        return buildByPage(pageInfo, queryWrapper, request);
    }

    /**
     * 构建分页返回结果
     * @param pageInfo
     * @param queryWrapper
     * @param request
     * @return
     */
    private PageVo<List<OrderInfoVo>> buildByPage(PageInfo pageInfo, LambdaQueryWrapper<OrderMaster> queryWrapper, OrderInfoSearchDto request) {
        if (ObjectUtils.isNotEmpty(request.getStateList())) {
            queryWrapper.in(OrderMaster::getOrderState, request.getStateList());
        }
        IPage<OrderMaster> page = new Page(pageInfo.getNumber(), pageInfo.getSize());
        IPage<OrderMaster> orderMasterIPage = orderMasterService.pageByQueryWrapper(queryWrapper, page);
        // 没有查询到数据
        if (ObjectUtils.isEmpty(orderMasterIPage.getRecords())) {
            return new PageVo<>(Lists.newArrayList(), page.getTotal(), page.getSize(), page.getCurrent());
        }
        List<OrderInfoVo> orderInfoVos = queryBuildOrderInfo(orderMasterIPage.getRecords(), request.getOrderSection());
        return new PageVo<>( orderInfoVos, page.getTotal(), page.getSize(), page.getCurrent());
    }

    /**
     * 根据 orderMaster 填充 其他数据
     * @param orderMasters
     * @param orderSectionRequest
     * @return
     */
    private List<OrderInfoVo> queryBuildOrderInfo(List<OrderMaster> orderMasters, OrderSectionRequest orderSectionRequest) {
        List<OrderInfoVo> orderInfoVos = orderMasterToOrderInfoVo(orderMasters);
        if (orderSectionRequest == null || !needBuildInfoDetail(orderSectionRequest)) {
            return orderInfoVos;
        }
        buildOrderOtherInfoConsumer.stream().forEach(execute -> execute.accept(orderInfoVos, orderSectionRequest));
        return orderInfoVos;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 获取订单详情 Function 添加到  Map 中
        ORDER_READER_HANDLER_MAP.put(OrderInfoQueryType.DB.getValue(), this);
        buildOrderAddressFunction();
        buildOrderFulfilFunction();
        buildOrderInvoiceFunction();
        buildOrderItemRelishFunction();
        buildOrderPaymentFunction();
        buildOrderDetailFunction();

    }


    /**
     * 构建地址信息查询 Function
     */
    private void buildOrderAddressFunction() {
        // address 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderAddressServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderAddress()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getId(), orderInfoVo -> orderInfoVo));
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
        buildOrderOtherInfoConsumer.add(orderAddressServiceConsumer);
    }

    /**
     * 构建履约单信息查询 Function
     */
    private void buildOrderFulfilFunction() {
        // fulfil 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderFulfilServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderFulfil()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getId(), orderInfoVo -> orderInfoVo));
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
        buildOrderOtherInfoConsumer.add(orderFulfilServiceConsumer);
    }

    /**
     * 构建发票查询 Function
     */
    private void buildOrderInvoiceFunction() {
        // invoice 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderInvoiceServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderInvoice()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getId(), orderInfoVo -> orderInfoVo));
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
        buildOrderOtherInfoConsumer.add(orderInvoiceServiceConsumer);
    }

    /**
     * 构建订单商品行优惠信息查询 Function
     */
    private void buildOrderItemRelishFunction() {
        // orderItemRelish 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderItemRelishServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderItemRelish()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getId(), orderInfoVo -> orderInfoVo));
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
        buildOrderOtherInfoConsumer.add(orderItemRelishServiceConsumer);
    }

    /**
     * 构建支付单查询 Function
     */
    private void buildOrderPaymentFunction() {
        // orderItemRelish 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderPaymentServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderPayment()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getId(), orderInfoVo -> orderInfoVo));
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
        buildOrderOtherInfoConsumer.add(orderPaymentServiceConsumer);
    }

    /**
     * 构建订单商品行查询 Function
     */
    private void buildOrderDetailFunction() {
        // orderItem 查询
        BiConsumer<List<OrderInfoVo>, OrderSectionRequest> orderItemServiceConsumer = (orderInfoVos, orderSectionRequest) -> {
            if (!orderSectionRequest.getOrderItem()) {
                return;
            }
            Map<Long, OrderInfoVo> collect = orderInfoVos.stream().collect(Collectors.toMap(orderInfoVo -> orderInfoVo.getOrderMasterVo().getId(), orderInfoVo -> orderInfoVo));
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
        buildOrderOtherInfoConsumer.add(orderItemServiceConsumer);
    }


}
