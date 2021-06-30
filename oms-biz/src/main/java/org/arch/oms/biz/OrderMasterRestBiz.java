package org.arch.oms.biz;

import org.arch.oms.common.request.OrderSaveRequest;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.manager.OrderCreateManager;
import org.arch.oms.manager.UserHelper;
import org.arch.oms.rest.OrderMasterRest;
import org.arch.oms.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;

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
        OrderSaveDto orderSaveDTO = orderCreateManager.buildOrderInfo(request, userHelper.getUserId(), userHelper.getUserName(), appId);
        // orderSaveDTO 入库


        return null;
    }

    public static void main(String[] args) {
        Field[] declaredFields = OrderSaveDto.class.getDeclaredFields();
    }
}
