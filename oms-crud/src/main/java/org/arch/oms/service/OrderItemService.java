package org.arch.oms.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudService;
import org.arch.oms.common.ContainerConstants;
import org.arch.oms.common.enums.CrudServiceTyp;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.dao.OrderItemDao;
import org.arch.oms.entity.OrderItem;
import org.arch.oms.service.bizinterface.OrderDetailQueryInterface;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lait
 * @description 项目业务(OrderItem) 表服务层
 * @date 2021年6月13日 下午7:02:32
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderItemService extends CrudService<OrderItem, Long> implements OrderDetailQueryInterface, InitializingBean {
    private final OrderItemDao orderItemDao;





    @Override
    public List<Object> queryByOrderId(Long orderNo) {
        LambdaQueryWrapper<OrderItem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderItem::getOrderNo, orderNo);
        return (List)orderItemDao.list(queryWrapper);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ContainerConstants.ORDER_ITEM_SERVICES.put(OrderItemTable.PERSONAL.getValue(), this);
        ContainerConstants.ORDER_ITEM_QUERY.put(OrderItemTable.PERSONAL.getValue(), this);
        ContainerConstants.CRUD_SERVICE_MAP.put(CrudServiceTyp.ORDER_ITEM.getValue(), this);
    }
}
