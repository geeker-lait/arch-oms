package org.arch.oms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
* @description 用户交易接口
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@RestController
@RequestMapping("transaction")
public interface TransactionApi{
    /**
     * 支付
     *
     * @param PayParamsRequest ZhiFuQingQiu
     * @return PayReponse ZhiFuXiangYing
     */
    @GetMapping("pay")
    PayReponse pay(PayParamsRequest payParamsRequest);

}
