package com.hq.ecmp.mscore.dto.cost;

import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.dto.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName 成本配置信息查询条件model
 * @Description TODO
 * @Author yj
 * @Date 2020/5/6 16:25
 * @Version 1.0
 */
@Data
@ApiModel(value = "成本配置信息查询条件model")
public class CostConfigQueryDto extends PageRequest {

    @ApiModelProperty(value = "城市code")
    private String cityCode;

    @ApiModelProperty(value = "成本名称")
    private String costConfigName;

    @ApiModelProperty(value = "服务类型")
    private String serviceType;

    @ApiModelProperty(value = "车型集合")
    private List<CostConfigCarTypeInfo> carTypes;

    @ApiModelProperty(value = "公司Id")
    private Long companyId;

    @ApiModelProperty(value = "包车类型")
    private String rentType;
}
