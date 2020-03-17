package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "项目成员模型")
public class ProjectUserVO {

    @ApiModelProperty(name = "name",value = "姓名")
    private String name;
    private Long userId;
    private Long projectId;
    @ApiModelProperty(name = "phonenumber",value = "手机")
    private String phonenumber;
    @ApiModelProperty(name = "jobNumber",value = "工号")
    private String jobNumber;
    @ApiModelProperty(name = "deptName",value = "部门")
    private String deptName;
    @ApiModelProperty(name = "company",value = "公司")
    private String company;

}
