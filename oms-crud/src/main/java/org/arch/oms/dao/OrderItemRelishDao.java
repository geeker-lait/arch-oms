package org.arch.oms.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudDao;
import org.arch.framework.crud.CrudServiceImpl;
import org.arch.oms.entity.OrderItemRelish;
import org.arch.oms.mapper.OrderItemRelishMapper;
import org.springframework.stereotype.Repository;

/**
 * @author lait
 * @description 项目业务(OrderItem) 表数据库访问层
 * @date 2021年6月13日 下午7:02:32
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class OrderItemRelishDao extends CrudServiceImpl<OrderItemRelishMapper, OrderItemRelish> implements CrudDao<OrderItemRelish> {
    private final OrderItemRelishMapper orderItemRelishMapper;
}