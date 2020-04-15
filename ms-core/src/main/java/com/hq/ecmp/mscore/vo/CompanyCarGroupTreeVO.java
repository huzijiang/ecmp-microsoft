package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/4/2 8:32
 */
@Data
public class CompanyCarGroupTreeVO {

    @ApiModelProperty(value = "组织id")
    private Long deptId;

    @ApiModelProperty(value = "上级组织id")
    private Long parentId;
    @ApiModelProperty(value = "企业自定义机构编码")
    private String  deptCode;
   // @ApiModelProperty(value = "祖级列表")
    //private String ancestors;
    @ApiModelProperty(value = "组织名称")
    private String deptName;

    @ApiModelProperty(value = "组织类别（1 公司 2 部门 3 车队）")
    private String deptType;

    @ApiModelProperty(value = "负责人用户id")
    private String leader;

    @ApiModelProperty(value = "负责人用户名字")
    private String leaderName;
    //子部门列表
    private List<CompanyCarGroupTreeVO> childrenList;
    //部门下的员工
    //private List<UserTreeVo> users;
    private List<CarGroupTreeVO> carGroupTreeVO;
}
