package org.arch.oms.rest;

import org.arch.framework.crud.CrudRest;
import org.arch.oms.api.dto.OrderTopicSearchDto;
import org.arch.oms.api.request.OrderTopicRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@RestController
@RequestMapping("orderTopic")
public interface OrderTopicRest extends CrudRest<OrderTopicRequest, Long, OrderTopicSearchDto> {


}
