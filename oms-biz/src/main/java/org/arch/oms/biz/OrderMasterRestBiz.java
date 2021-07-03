package org.arch.oms.biz;

import org.apache.commons.lang3.ObjectUtils;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.ExceptionStatusCode;
import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.manager.OrderCreateManager;
import org.arch.oms.manager.UserHelper;
import org.arch.oms.rest.OrderMasterRest;
import org.arch.oms.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author junboXiang
 * @version V1.0
 * 2021-06-24
 */
@Service
public class OrderMasterRestBiz implements OrderMasterRest {

    @Autowired
    private UserHelper userHelper;
    @Autowired
    private OrderCreateManager orderCreateManager;
    @Autowired
    private OrderMasterService orderMasterService;


    @Override
    public Boolean save(@RequestBody OrderSaveRequest request) {
        Long appId = userHelper.getAppId();
        final int maxProductSize = 30;
        if (ObjectUtils.isNotEmpty(request.getProductNoList()) && request.getProductNoList().size() > maxProductSize) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode("同一订单最多提交30个商品"));
        }
        OrderSaveDto orderSaveDTO = orderCreateManager.buildOrderInfo(request, userHelper.getUserId(), userHelper.getUserName(), appId);
        // 订单和订单附属信息 入库
        orderMasterService.saveOrderInfo(orderSaveDTO);
        return null;
    }

}
