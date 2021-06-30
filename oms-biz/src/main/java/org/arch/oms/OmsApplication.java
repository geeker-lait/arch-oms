package org.arch.oms;

import org.arch.oms.service.OrderMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-24
 */
@SpringBootApplication
public class OmsApplication {
    @Autowired
    private OrderMasterService orderMasterService;

    public static void main(String[] args) {
        SpringApplication.run(OmsApplication.class, args);
    }
}