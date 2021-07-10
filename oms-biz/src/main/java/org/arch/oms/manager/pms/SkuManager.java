package org.arch.oms.manager.pms;

import lombok.extern.slf4j.Slf4j;
import org.arch.pms.admin.api.SkuFeignApi;
import org.arch.pms.admin.api.req.ProductSkuReq;
import org.arch.pms.admin.api.res.ProductSkuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * pms  sku信息查询
 * @author junboXiang
 * @version V1.0
 * 2021-07-09
 */
@Slf4j
@Component
public class SkuManager {

    @Autowired
    private SkuFeignApi skuFeignApi;

    /**
     * 根据单个sku 查询 sku信息
     * @param skuNo
     * @return
     */
    public ProductSkuVo getBySkuCode(String skuNo) {
        ProductSkuReq productSkuReq = new ProductSkuReq();
        productSkuReq.setSkuNo(skuNo);
        return skuFeignApi.skuGet(productSkuReq).getData();
    }

    /**
     * 根据sku集合查询 sku信息  pms 暂时没有批量接口，使用循环查询
     * fixme
     * @param skuNoList
     * @return
     */
    public List<ProductSkuVo> getProductSkuList(Collection<String> skuNoList) {
        ProductSkuReq productSkuReq = new ProductSkuReq();
        return skuNoList.stream().map(skuNo -> {
            productSkuReq.setSkuNo(skuNo);
            return skuFeignApi.skuGet(productSkuReq).getData();
        }).collect(Collectors.toList());
    }



}
