package org.arch.oms.api;

import org.arch.framework.beans.Response;
import org.arch.oms.common.request.OrderCartSaveRequest;
import org.arch.oms.common.vo.OrderCartVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 购物车操作
 * @author junboXiang
 * @version V1.0
 * 2021-07-07
 */

public interface OrderCartApi {

    /**
     * 批量保存 用户 购物车
     * @param requests
     * @return
     */
    @PostMapping("/save")
    Response<Boolean> save(@RequestBody List<OrderCartSaveRequest> requests);


    /**
     * 删除购物车id集合
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    Response<Boolean> delete(@RequestBody List<Long> ids);


    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping("get")
    Response<List<OrderCartVo>> getCartList();
}
