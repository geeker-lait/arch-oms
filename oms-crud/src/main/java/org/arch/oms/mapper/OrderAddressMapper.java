package org.arch.oms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.arch.framework.crud.CrudMapper;
import org.arch.oms.entity.OrderAddress;

/**
 * <p>
 * 订单-收发货方信息 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Mapper
public interface OrderAddressMapper extends CrudMapper<OrderAddress> {

}
