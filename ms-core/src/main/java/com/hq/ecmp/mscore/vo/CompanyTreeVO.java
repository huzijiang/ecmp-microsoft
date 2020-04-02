package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/4/2 0:58
 */
@Data
public class CompanyTreeVO {

    @ApiModelProperty(value = "组织id")
    private Long deptCompanyId;

    // @ApiModelProperty(value = "上级组织id")
    // private Long parentId;

    //  @ApiModelProperty(value = "企业自定义机构编码")
    // private String  deptCode;

    // @ApiModelProperty(value = "祖级列表")
    // private String ancestors;

    @ApiModelProperty(value = "组织名称")
    private String deptCompanyName;

    @ApiModelProperty(value = "组织类别（1 公司 2 部门 3 车队）")
    private String deptType;

    //  @ApiModelProperty(value = "负责人")
    //  private String leader;

    //子公司列表
    private List<CompanyTreeVO> childrenList;

    private List<CarGroupTreeVO> carGroupTreeVO;
}
