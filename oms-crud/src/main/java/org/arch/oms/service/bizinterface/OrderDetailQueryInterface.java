package org.arch.oms.service.bizinterface;

import java.util.List;

/**
 * 订单子项表 查询接口
 * @author junboXiang
 * @version V1.0
 * 2021-06-27
 */
public interface OrderDetailQueryInterface {

    /**
     * 根据订单id获取 获取具体的订单类型接口
     * 子表可能有多张，返回值 用object 查询后用对应类型的 处理 类 convert
     * @param orderNo
     * @return
     */
    List<Object> queryByOrderId(Long orderNo);





}
