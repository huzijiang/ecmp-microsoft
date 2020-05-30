package com.hq.ecmp.mscore.dto.cost;

import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
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
@ApiModel(value = "价格总览model")
public class CostConfigQueryPriceDto {
    //起始页码
    @ApiModelProperty(name = "pageNum", value = "起始页码",required = false)
    private Integer pageNum;
    //每页显示条数
    @ApiModelProperty(name = "pageSize", value = "每页显示条数",required = false)
    private Integer pageSize;

    @ApiModelProperty(value = "城市code")
    private String cityCode;

    @ApiModelProperty(value = "成本名称")
    private String costConfigName;

    @ApiModelProperty(value = "服务类型")
    private String serviceType;
    @ApiModelProperty(value = "车队id")
    private Long carGroupId;

    @ApiModelProperty(value = "车型")
    private Long carTypeId;

    @ApiModelProperty(value = "公司Id")
    private Long companyId;

    @ApiModelProperty(value = "包车类型")
    private String rentType;
    @ApiModelProperty(value = "服务模式(车+驾驶员,仅驾驶员,仅车)")
    private String carGroupUserMode;

}
