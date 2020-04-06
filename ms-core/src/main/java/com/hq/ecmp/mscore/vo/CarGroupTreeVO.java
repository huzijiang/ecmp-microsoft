package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/4/1 22:59
 */
@Data
@ApiModel("车队树模型")
public class CarGroupTreeVO {

    @ApiModelProperty(value = "组织id")
    private Long carGroupId;

    @ApiModelProperty(value = "组织名称")
    private String carGroupName;



    //子部门列表
    private List<CarGroupTreeVO> childrenList;

}
