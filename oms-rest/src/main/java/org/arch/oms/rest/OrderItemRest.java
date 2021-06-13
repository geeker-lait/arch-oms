package org.arch.oms.rest;

import org.arch.framework.crud.CrudRest;
import org.arch.oms.api.dto.OrderItemSearchDto;
import org.arch.oms.api.request.OrderItemRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@RestController
@RequestMapping("orderItem")
public interface OrderItemRest extends CrudRest<OrderItemRequest, Long, OrderItemSearchDto> {


}
