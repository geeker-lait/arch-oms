package org.arch.oms.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.arch.framework.crud.CrudService;
import org.arch.oms.annotation.CrudServiceTypAnnotation;
import org.arch.oms.common.ContainerConstants;
import org.arch.oms.common.enums.CrudServiceTyp;
import org.arch.oms.dao.OrderCartDao;
import org.arch.oms.dao.OrderMasterDao;
import org.arch.oms.dto.OrderSaveDto;
import org.arch.oms.entity.OrderMaster;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author lait
 * @description 项目业务(OrderMaster) 表服务层
 * @date 2021年6月13日 下午7:02:32
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderMasterService extends CrudService<OrderMaster, Long> implements InitializingBean {
    private final OrderMasterDao orderMasterDao;
    private final OrderCartDao orderCartDao;

    /**
     * 保存订单 及 订单附属信息
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrderInfo(OrderSaveDto dto) {
        try {
            Field[] declaredFields = dto.getClass().getDeclaredFields();
            if (declaredFields.length < 1) {
                return Boolean.TRUE;
            }
            for (int i = 0, length = declaredFields.length; i < length; i++) {
                Field field = declaredFields[i];
                CrudServiceTypAnnotation annotation = field.getAnnotation(CrudServiceTypAnnotation.class);
                if (annotation == null) {
                    continue;
                }
                CrudServiceTyp type = annotation.type();
                if (type == null) {
                    continue;
                }
                CrudService crudService = ContainerConstants.CRUD_SERVICE_MAP.get(type.getValue());
                field.setAccessible(Boolean.TRUE);
                Object object = field.get(dto);
                if (crudService == null || ObjectUtils.isEmpty(object)) {
                    continue;
                }
                if (field.getType() == List.class) {
                    crudService.saveList((List)object);
                } else {
                    crudService.save(object);
                }
            }
            if (CollectionUtils.isNotEmpty(dto.getOrderItem())) {
                ContainerConstants.ORDER_ITEM_SERVICES.get(dto.getOrderDetailTable()).saveList(dto.getOrderItem());
            }            if (CollectionUtils.isNotEmpty(dto.getRemoveCart())) {

                orderCartDao.removeByIds(dto.getRemoveCart());
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.info("订单保存失败 params:{}", JSON.toJSONString(dto), e);
            return Boolean.FALSE;
        }
    }

    /**
     * 分页查询订单
     * @param queryWrapper
     * @param page
     * @return
     */
    public IPage<OrderMaster> pageByQueryWrapper(Wrapper<OrderMaster> queryWrapper, IPage<OrderMaster> page) {
        return orderMasterDao.page(page, queryWrapper);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ContainerConstants.CRUD_SERVICE_MAP.put(CrudServiceTyp.ORDER_MASTER.getValue(), this);
    }
}
