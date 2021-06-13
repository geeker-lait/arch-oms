package org.arch.oms.rest;

import org.arch.framework.crud.CrudRest;
import org.arch.oms.api.dto.OrderAdressSearchDto;
import org.arch.oms.api.request.OrderAdressRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@RestController
@RequestMapping("orderAdress")
public interface OrderAdressRest extends CrudRest<OrderAdressRequest, Long, OrderAdressSearchDto> {


}
