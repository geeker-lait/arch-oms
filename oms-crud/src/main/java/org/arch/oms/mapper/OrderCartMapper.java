package org.arch.oms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.arch.framework.crud.CrudMapper;
import org.arch.oms.entity.OrderCart;

/**
 * <p>
 * 订单-购物车 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Mapper
public interface OrderCartMapper extends CrudMapper<OrderCart> {

}
