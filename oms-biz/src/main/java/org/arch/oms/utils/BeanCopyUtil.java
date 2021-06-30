package org.arch.oms.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Slf4j
public class BeanCopyUtil {


    private static Lock initLock = new ReentrantLock();

    private static Map<String, BeanCopier> beanCopierMap = new HashMap<>();

    /**
     * 初始化 BeanCopier
     *
     * @param source
     * @param target
     * @return
     */
    private static BeanCopier initCopier(Class source, Class target) {
        initLock.lock();
        try{
            BeanCopier find = beanCopierMap.get(source.getName() + "_" + target.getName());
            if (find != null) {
                return find;
            }
            BeanCopier beanCopier = BeanCopier.create(source, target, false);
            beanCopierMap.put(source.getName() + "_" + target.getName(), beanCopier);
            return beanCopier;
        }finally {
            initLock.unlock();
        }
    }


    /**
     * 获取BeanCopier
     *
     * @param source
     * @param target
     * @return
     */
    private static BeanCopier getBeanCopier(Class source, Class target) {
        final BeanCopier beanCopier = beanCopierMap.get(source.getClass().getName() + "_" + target.getName());
        if (beanCopier != null) {
            return beanCopier;
        }
        return initCopier(source, target);
    }


    /**
     * Pojo 类型转换（浅复制，字段名&类型相同则被复制）
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T convert(final Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        final BeanCopier beanCopier = getBeanCopier(source.getClass(), targetClass);
        try {
            T target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;

        } catch (Exception e) {
            log.error("对象拷贝失败", e);
            throw new RuntimeException("对象拷贝失败" + source + "_" + targetClass);
        }
    }

    /**
     * Pojo 类型转换（浅复制，字段名&类型相同则被复制）
     *
     * @param sourceList
     * @param targetClass
     * @param <E>
     * @return
     */
    public static <E> List<E> convert(List sourceList, Class<E> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        try {
            List<E> result = new ArrayList<>();
            sourceList.forEach(e -> result.add(convert(e, targetClass)));
            return result;
        } catch (Exception e) {
            log.error("对象拷贝失败", e);
            throw new RuntimeException("对象拷贝失败" + sourceList + "_" + targetClass);
        }
    }


}
