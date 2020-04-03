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
    private Long deptGroupId;

   // @ApiModelProperty(value = "上级组织id")
   // private Long parentId;

  //  @ApiModelProperty(value = "企业自定义机构编码")
   // private String  deptCode;

   // @ApiModelProperty(value = "祖级列表")
   // private String ancestors;

    @ApiModelProperty(value = "组织名称")
    private String deptGroupName;

    @ApiModelProperty(value = "组织类别（1 公司 2 部门 3 车队）")
    private String deptType;

    @ApiModelProperty(value = "车队人数")
    private Integer count;

    //子部门列表
    private List<CarGroupTreeVO> childrenList;

}
