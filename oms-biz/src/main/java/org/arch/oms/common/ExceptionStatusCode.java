package org.arch.oms.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.arch.framework.beans.enums.StatusCode;

/**
 *
 * @author junboXiang
 * @version V1.0
 * 2021-06-26
 */
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionStatusCode implements StatusCode {

    private int code;

    private String descr;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescr() {
        return descr;
    }

    public static ExceptionStatusCode getDefaultExceptionCode(String descr) {
        return new ExceptionStatusCode(500, descr);
    }
}
