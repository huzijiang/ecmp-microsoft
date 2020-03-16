package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ContactorDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/12 11:56
 * @Version 1.0
 */
@Data
@ApiModel(value = "联系人属性dto")
public class ContactorDto {


    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "电话")
    private String  phone;
    @ApiModelProperty(value = "角色")
    private String roleName;
}
