package org.arch.oms.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
* @description 通道接口
*
* @author lait
* @date 2021年6月13日 下午7:02:32
*/
@RestController
@RequestMapping("merchantChannel")
public interface MerchantChannelApi{
    /**
     * 申请开通账号
     *
     * @param ApplyMerchantRequest ShenQingShangHuQingQiu
     * @return Void 
     */
    @GetMapping("applyAccount")
    Void applyAccount(ApplyMerchantRequest applyMerchantRequest);

}
