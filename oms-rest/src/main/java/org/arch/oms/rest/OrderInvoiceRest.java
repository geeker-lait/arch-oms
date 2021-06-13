package org.arch.oms.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
* @description 
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@RestController
@RequestMapping("orderInvoice")
public interface OrderInvoiceRest extends CrudRest<OrderInvoiceRequest, Long, OrderInvoiceSearchDto>{


}
