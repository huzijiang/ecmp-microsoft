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
    private Long deptId;

    @ApiModelProperty(value = "组织名称")
    private String deptName;

    @ApiModelProperty(value = "组织类别（1 公司 2 部门 3 车队）  满足前端")
    private String deptType;


    //子部门列表
    private List<CarGroupTreeVO> carGroupTreeVO;

}
