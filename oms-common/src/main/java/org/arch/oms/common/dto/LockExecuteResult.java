package org.arch.oms.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 加锁执行 结果
 * @author junboXiang
 * @version V1.0
 * 2021-07-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockExecuteResult<T> {
    boolean lockSuccess;

    T result;
}
