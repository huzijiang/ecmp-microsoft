package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申请用车时，根据用车人模糊查询返回DTO
 *
 * @author Chenkp
 * @date 2020-07-20 13:46
 */
@Data
@ApiModel(value = "根据用车人模糊查询返回DTO")
public class JourneyPassengerInfoDto {

    @ApiModelProperty(name = "name", value = "名称")
    private String name;

    @ApiModelProperty(name = "mobile", value = "手机号")
    private String mobile;

}