package com.hq.ecmp.mscore.dto.cost;

import com.hq.ecmp.mscore.domain.CostConfigCarTypeInfo;
import com.hq.ecmp.mscore.domain.CostConfigCityInfo;
import com.hq.ecmp.mscore.domain.CostConfigInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/6 12:59
 * @Version 1.0
 */
@Data
@ApiModel(value = "成本中心配置信息插入集合model")
public class CostConfigInsertDto extends CostConfigInfo {
    @ApiModelProperty(value = "城市集合")
    List<CostConfigCityInfo> cities;
    @ApiModelProperty(value = "车型集合")
    List<CostConfigCarTypeInfo> carTypes;

}
