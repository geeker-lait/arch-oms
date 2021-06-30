package org.arch.oms.common.enums;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-06-24
 */
public interface ValueDescription<T> {

    /**
     * 获取值
     * @return
     */
    T getValue();

    /**
     * 获取描述
     * @return
     */
    String getDescription();
}
