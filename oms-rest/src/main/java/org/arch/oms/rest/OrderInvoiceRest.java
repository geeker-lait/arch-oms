package org.arch.oms.rest;

import org.arch.framework.crud.CrudRest;
import org.arch.oms.api.dto.OrderInvoiceSearchDto;
import org.arch.oms.api.request.OrderInvoiceRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@RestController
@RequestMapping("orderInvoice")
public interface OrderInvoiceRest extends CrudRest<OrderInvoiceRequest, Long, OrderInvoiceSearchDto> {


}
