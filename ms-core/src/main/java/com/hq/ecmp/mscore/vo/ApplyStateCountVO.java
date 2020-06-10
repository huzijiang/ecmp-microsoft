package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "发票列表模型")
public class ApplyStateCountVO {

    private String dispatchCount;
    private String DispatchedCount;
    private String rejectAmount;
    private String cancelAmount;
    private String overTimeAmount;


}
