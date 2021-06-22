package org.arch.oms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.arch.framework.crud.CrudMapper;
import org.arch.oms.entity.OrderItemRelish;

/**
 * <p>
 * 订单商品行-佐料 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Mapper
public interface OrderItemRelishMapper extends CrudMapper<OrderItemRelish> {

}
