package org.arch.oms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudService;
import org.arch.oms.dao.OrderAdressDao;
import org.arch.oms.entity.OrderAdressEntity;
import org.springframework.stereotype.Service;

/**
 * @author lait
 * @description 项目业务(OrderAdress) 表服务层
 * @date 2021年6月13日 下午7:02:32
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderAdressService extends CrudService<OrderAdressEntity, Long> {
    private final OrderAdressDao orderAdressDao = (OrderAdressDao) crudDao;
}
