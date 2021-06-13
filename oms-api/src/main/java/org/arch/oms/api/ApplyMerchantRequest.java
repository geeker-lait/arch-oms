package org.arch.oms.api;

import org.arch.framework.api.crud.BaseRequestDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * ApplyMerchantRequest
 * ShenQingShangHuQingQiu
 * @author 
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ApplyMerchantRequest extends BaseRequestDto {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 主体ID
     */
    private Long principalId;

    /**
     * 平台商户号
     */
    private String merchantNo;

}
