package com.hq.ecmp.mscore.dto.cost;

import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
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
@ApiModel(value = "成本配置判重条件model")
public class CostConfigQueryDoubleValidDto  {
    //起始页码
    @ApiModelProperty(name = "pageNum", value = "起始页码",required = false)
    private Integer pageNum;
    //每页显示条数
    @ApiModelProperty(name = "pageSize", value = "每页显示条数",required = false)
    private Integer pageSize;
    //0:当天,1:明天,2:后天 3:大后天,-1:全部

    //搜索字段
    @ApiModelProperty(name = "search", value = "搜索关键字",required = false)
    private String search;

    @ApiModelProperty(value = "城市集合")
    List<CostConfigCityInfo> cities;

    @ApiModelProperty(value = "成本名称")
    private String costConfigName;

    @ApiModelProperty(value = "服务类型")
    private String serviceType;
    @ApiModelProperty(value = "车队id")
    private Long carGroupId;

    @ApiModelProperty(value = "车型集合")
    private List<CostConfigCarTypeInfo> carTypes;

    @ApiModelProperty(value = "公司Id")
    private String companyId;

    @ApiModelProperty(value = "包车类型")
    private String rentType;
}
