package com.hq.ecmp.mscore.dto.cost;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/18 11:44
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidDoubleDtoResult {
    /**
     * 城市名字
     */
    private String cityName;
    private Long costId;
    /**
     * 车型名字
     */
    private String carTypeName;
    private String carTypeIds;
    @ApiModelProperty(name = "carGroupName",value = "车队名称")
    private String carGroupName;
    private String carGroupId;
    /**
     * 服务类型
     */
    private String serviceType;
    @ApiModelProperty(name = "charterCarType",value = "T001 半日租（4小时）T002 整日租（8小时）")
    private String charterCarType;
    @ApiModelProperty(name = "carGroupUserMode",value = "服务模式(CA00车和驾驶员都用,CA01只用车,CA10  只用驾驶员)")
    private String carGroupUserMode;

}
