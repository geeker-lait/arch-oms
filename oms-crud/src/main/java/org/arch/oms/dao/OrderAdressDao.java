package org.arch.oms.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudDao;
import org.arch.framework.crud.CrudServiceImpl;
import org.arch.oms.entity.OrderAdressEntity;
import org.arch.oms.mapper.OrderAdressMapper;
import org.springframework.stereotype.Repository;

/**
 * @author lait
 * @description 项目业务(OrderAdress) 表数据库访问层
 * @date 2021年6月13日 下午7:02:32
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class OrderAdressDao extends CrudServiceImpl<OrderAdressMapper, OrderAdressEntity> implements CrudDao<OrderAdressEntity> {
    private final OrderAdressMapper orderAdressMapper;
}