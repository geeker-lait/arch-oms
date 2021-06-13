package org.arch.oms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.arch.framework.crud.CrudMapper;
import org.arch.oms.entity.OrderPaymentEntity;

/**
 * 订单-支付记录表(OrderPayment) 表数据库 Mapper 层
 *
 * @author lait
 * @date 2021年6月13日 下午7:02:32
 * @since 1.0.0
 */
@Mapper
public interface OrderPaymentMapper extends CrudMapper<OrderPaymentEntity> {

}