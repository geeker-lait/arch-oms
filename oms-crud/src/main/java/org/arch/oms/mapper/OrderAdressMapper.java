package org.arch.oms.mapper;

import org.arch.framework.crud.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.arch.oms.entity.OrderAdressEntity;

/**
 * 订单-收发货方信息(OrderAdress) 表数据库 Mapper 层
 *
 * @author lait
 * @date 2021年6月13日 下午7:02:32
 * @since  1.0.0
 */
@Mapper
public interface OrderAdressMapper extends CrudMapper<OrderAdressEntity> {

}