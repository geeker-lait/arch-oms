package org.arch.oms.manager.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.arch.oms.common.enums.OrderItemTable;
import org.arch.oms.common.vo.OrderDetailVo;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.entity.OrderItem;
import org.arch.oms.manager.OrderDetailHandler;
import org.arch.oms.utils.BeanCopyUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-29
 */
@Slf4j
@Component
public class OrderItemHandler extends OrderDetailHandler implements InitializingBean {

    @Override
    public List<OrderDetailVo> convertVo(List<Object> detail) {
        if (CollectionUtils.isEmpty(detail)) {
            return Lists.newArrayList();
        }
        List<OrderItem> collect = detail.stream().map(obj -> (OrderItem) obj).collect(Collectors.toList());
        return BeanCopyUtil.convert(collect, OrderDetailVo.class);
    }

    @Override
    public List<Object> convertDo(List<Object> products) {
        return null;
    }

    @Override
    public BigDecimal buildOrderDetailRelish(List<Object> products, OrderSaveDto orderSaveDto) {
        // todo 现在没有处理 只返回商品行销售价格相加， 校验积分等数据

        return BigDecimal.ZERO;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CONVERT_ORDER_DETAIL_HANDLERS.put(OrderItemTable.PERSONAL.getValue(), this);
    }
}
