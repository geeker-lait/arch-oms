package org.arch.oms.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author junboXiang
 * @version V1.0
 * 2021-07-03
 */
@Data
public class PageVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T data;
    protected long total;
    protected long size;
    protected long current;

    public PageVo() {

    }

    public PageVo(T data, long total, long size, long current) {
        this.data = data;
        this.total = total;
        this.size = size;
        this.current = current;
    }
}
