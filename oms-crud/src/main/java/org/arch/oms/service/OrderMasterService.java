package org.arch.oms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudService;
import org.arch.oms.entity.OrderMasterEntity;
import org.springframework.stereotype.Service;

/**
* @description 项目业务(OrderMaster) 表服务层
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderMasterService extends CrudService<OrderMasterEntity, Long> {
    private final OrderMasterDao orderMasterDao = (OrderMasterDao) crudDao;
}
