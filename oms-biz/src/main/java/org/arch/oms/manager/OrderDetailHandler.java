package org.arch.oms.manager;

import org.arch.oms.common.vo.OrderDetailVo;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.pms.admin.api.res.ProductSkuVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-28
 */
public abstract class OrderDetailHandler {

    protected static final Map<String, OrderDetailHandler> CONVERT_ORDER_DETAIL_HANDLERS = new ConcurrentHashMap<>();


    public static OrderDetailHandler getHandler(String orderItemTable) {
        return CONVERT_ORDER_DETAIL_HANDLERS.get(orderItemTable);
    }

    /**
     * 将Do 转换冲 vo
     *
     * @param detail
     * @return
     */
    public abstract List<OrderDetailVo> convertVo(List<Object> detail);

    /**
     * 创建购物车 根据 主表类型 创建对应的订单详情记录
     * @param skuVoBigDecimalMap
     * @param orderSaveDto
     */
    public abstract void convertDoByProductSkuVo(Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap, OrderSaveDto orderSaveDto);

    /**
     * 根据不同类型的商品行详情 创建 商品优惠信息，并返回最终的订单价格
     * @param skuVoBigDecimalMap
     * @param orderSaveDto
     * @return
     */
    public abstract BigDecimal buildOrderDetailRelish (Map<ProductSkuVo, BigDecimal> skuVoBigDecimalMap, OrderSaveDto orderSaveDto);

    protected static int getOderItemLength(int productNum) {
        return String.valueOf(productNum).length() + 1;
    }

}
