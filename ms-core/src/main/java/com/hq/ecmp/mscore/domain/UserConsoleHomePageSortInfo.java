package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 后台首页顺序model
 */
@Data
@ApiModel(description = "后台首页顺序model")
public class UserConsoleHomePageSortInfo {

    @ApiModelProperty(name = "Id", value = "主键id")
    private Long Id;

    @ApiModelProperty(name = "userId", value = "用户id")
    private Long userId;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Long deptId;

    @ApiModelProperty(name = "companyId", value = "公司id")
    private Long companyId;

    @ApiModelProperty(name = "panelId", value = "模块id")
    private Long panelId;

    @ApiModelProperty(name = "panelName", value = "模块名称")
    private String  panelName;

    @ApiModelProperty(name = "sortNum", value = "排序")
    private Integer  sortNum;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private Long createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "修改人")
    private Long updateBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间")
    private Date updateTime;
}
