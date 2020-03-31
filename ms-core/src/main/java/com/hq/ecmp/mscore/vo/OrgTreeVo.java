package com.hq.ecmp.mscore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("部门树模型")
public class OrgTreeVo {

    @ApiModelProperty(value = "组织id")
    private Long deptId;
    @ApiModelProperty(value = "上级组织id")
    private Long parentId;
    @ApiModelProperty(value = "企业自定义机构编码")
    private String  deptCode;
    @ApiModelProperty(value = "祖级列表")
    private String ancestors;
    @ApiModelProperty(value = "组织名称")
    private String deptName;
    @ApiModelProperty(value = "组织类别（1 公司 2 部门 3 车队）")
    private String deptType;
    @ApiModelProperty(value = "负责人")
    private String leader;
    //子部门列表
    private List<OrgTreeVo> childrenList;
    //部门下的员工
    private List<UserTreeVo> users;
}

