package org.arch.oms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudService;
import org.arch.oms.common.ContainerConstants;
import org.arch.oms.common.enums.CrudServiceTyp;
import org.arch.oms.dao.OrderPaymentDao;
import org.arch.oms.entity.OrderPayment;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author lait
 * @description 项目业务(OrderPayment) 表服务层
 * @date 2021年6月13日 下午7:02:32
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderPaymentService extends CrudService<OrderPayment, Long> implements InitializingBean {
    private final OrderPaymentDao orderPaymentDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        ContainerConstants.CRUD_SERVICE_MAP.put(CrudServiceTyp.ORDER_PAYMENT.getValue(), this);

    }
}
