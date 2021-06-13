package org.arch.oms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.arch.framework.crud.CrudService;
import org.arch.oms.dao.OrderTopicDao;
import org.arch.oms.entity.OrderTopicEntity;
import org.springframework.stereotype.Service;

/**
 * @author lait
 * @description 项目业务(OrderTopic) 表服务层
 * @date 2021年6月13日 下午7:02:32
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderTopicService extends CrudService<OrderTopicEntity, Long> {
    private final OrderTopicDao orderTopicDao = (OrderTopicDao) crudDao;
}