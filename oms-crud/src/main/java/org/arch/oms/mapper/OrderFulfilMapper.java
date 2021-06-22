package org.arch.oms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.arch.framework.crud.CrudMapper;
import org.arch.oms.entity.OrderFulfil;

/**
 * <p>
 * 订单-履约信息（快递or物流or其他） Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Mapper
public interface OrderFulfilMapper extends CrudMapper<OrderFulfil> {

}
