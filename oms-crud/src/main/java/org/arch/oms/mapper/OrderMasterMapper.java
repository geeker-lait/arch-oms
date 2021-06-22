package org.arch.oms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.arch.framework.crud.CrudMapper;
import org.arch.oms.entity.OrderMaster;

/**
 * <p>
 * 订单-订单主体 Mapper 接口
 * </p>
 *
 * @author admin
 * @since 2021-06-22
 */
@Mapper
public interface OrderMasterMapper extends CrudMapper<OrderMaster> {

}
