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
    private Long id;
    @ApiModelProperty(value = "上级组织id")
    private Long parentId;
    @ApiModelProperty(value = "组织名称")
    private String showname;
    @ApiModelProperty(value = "组织类别0公司1员工")
    private String type;
    //子部门列表
    private List<OrgTreeVo> children;
    //部门下的员工
//    private List<UserTreeVo> users;
}

