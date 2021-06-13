package org.arch.oms.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudDao;
import org.arch.framework.crud.CrudServiceImpl;
import org.arch.oms.entity.OrderPaymentEntity;
import org.arch.oms.mapper.OrderPaymentMapper;
import org.springframework.stereotype.Repository;

/**
 * @author lait
 * @description 项目业务(OrderPayment) 表数据库访问层
 * @date 2021年6月13日 下午7:02:32
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class OrderPaymentDao extends CrudServiceImpl<OrderPaymentMapper, OrderPaymentEntity> implements CrudDao<OrderPaymentEntity> {
    private final OrderPaymentMapper orderPaymentMapper;
}