package org.arch.oms.dao;

import org.arch.oms.entity.OrderCartEntity;
import org.arch.oms.mapper.OrderCartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudDao;
import org.arch.framework.crud.CrudServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @description 项目业务(OrderCart) 表数据库访问层
 *
 * @author lait
 * @date 2021年6月13日 下午7:02:32
 * @since  1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class OrderCartDao extends CrudServiceImpl<OrderCartMapper, OrderCartEntity> implements CrudDao<OrderCartEntity>{
    private final OrderCartMapper orderCartMapper;
}