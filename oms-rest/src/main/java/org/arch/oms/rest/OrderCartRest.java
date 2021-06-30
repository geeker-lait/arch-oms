package org.arch.oms.rest;

import org.arch.oms.common.request.OrderCartSaveRequest;
import org.arch.oms.common.vo.OrderCartVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@RestController
@RequestMapping("orderCart")
public interface OrderCartRest {


    /**
     * 批量保存 购物车
     * @param requests
     * @return
     */
    @PostMapping("/save")
    Boolean save(@RequestBody List<OrderCartSaveRequest> requests);


    @PostMapping("/delete")
    Boolean delete(@RequestBody List<Long> ids);


    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping("get")
    List<OrderCartVo> getCartList();


}
