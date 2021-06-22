package org.arch.oms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.arch.framework.crud.CrudMapper;
import org.arch.oms.entity.OrderItem;

/**
 * <p>
 * 订单-销售订单项 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Mapper
public interface OrderItemMapper extends CrudMapper<OrderItem> {

}
