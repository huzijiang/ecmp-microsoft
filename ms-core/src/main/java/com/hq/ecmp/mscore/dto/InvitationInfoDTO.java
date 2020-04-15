package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 邀请类别
 * @author shixin
 * @date 2020/3/17
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvitationInfoDTO {
    //邀请类型
    @NotNull
    @ApiModelProperty(name = "type", value = "T001员工,T002驾驶员",required = true)
    private String type;


}
