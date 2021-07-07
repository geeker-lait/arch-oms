package org.arch.oms.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.arch.oms.common.request.OrderAddressRequest;
import org.arch.oms.common.vo.OrderAddressVo;
import org.arch.oms.entity.OrderAddress;
import org.arch.oms.rest.OrderAddressRest;
import org.arch.oms.service.OrderAddressService;
import org.arch.oms.utils.BeanCopyUtil;
import org.arch.oms.utils.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lait
 * @description
 * @date 2021年6月13日 下午7:02:32
 */
@Slf4j
@RestController
@RequestMapping("/oms/address")
public class OrderAddressRestBiz implements OrderAddressRest {


    @Autowired
    private OrderAddressService orderAddressService;

    @Override
    public List<OrderAddressVo> getOrderAddressByRequest(@RequestBody OrderAddressRequest request) {
        ValidatorUtil.checkNull(request, "请填写查询参数");
        ValidatorUtil.checkNull(request.getOrderNo(), "请填写查询参数");
        ValidatorUtil.checkNull(request.getUserId(), "请填写查询用户id");
        LambdaQueryWrapper<OrderAddress> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(OrderAddress::getOrderNo, request.getOrderNo());
        List<OrderAddress> list = orderAddressService.findAllBySpec(queryWrapper);
        return BeanCopyUtil.convert(list, OrderAddressVo.class);
    }
}
