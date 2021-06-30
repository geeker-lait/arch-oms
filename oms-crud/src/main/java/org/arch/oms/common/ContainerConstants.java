package org.arch.oms.common;

import org.arch.framework.crud.CrudService;
import org.arch.oms.service.bizinterface.OrderDetailQueryInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * service 常量
 * @author junboXiang
 * @version V1.0
 * 2021-06-24
 */
public class ContainerConstants {

    /**
     * 订单主表 order_table 创建 订单子项
     */
    public static final Map<String, CrudService> ORDER_ITEM_SERVICES = new ConcurrentHashMap<>();

    /**
     * 订单子项  查询 map
     * key 表类型  value 具体实现的子查询类
     */
    public static final Map<String, OrderDetailQueryInterface> ORDER_ITEM_QUERY = new ConcurrentHashMap<>();

    /**
     * 所有service  名称 和 处理类对应关系
     */
    public static final Map<String, CrudService> CRUD_SERVICE_MAP = new ConcurrentHashMap<>();

}
