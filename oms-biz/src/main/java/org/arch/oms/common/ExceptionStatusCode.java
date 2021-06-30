package org.arch.oms.common;

import lombok.AllArgsConstructor;
import org.arch.framework.beans.enums.StatusCode;

/**
 *
 * @author junboXiang
 * @version V1.0
 * 2021-06-26
 */
@AllArgsConstructor
public class ExceptionStatusCode implements StatusCode {

    private int code;

    private String descr;




    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getDescr() {
        return null;
    }

    public static ExceptionStatusCode getDefaultExceptionCode(String descr) {
        return new ExceptionStatusCode(500, descr);
    }
}
