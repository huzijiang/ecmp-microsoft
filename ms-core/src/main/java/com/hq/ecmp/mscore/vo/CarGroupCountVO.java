package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/4/2 23:16
 */
@Data
public class CarGroupCountVO {


    @ApiModelProperty(name = "deptCode",value = "企业自定义机构编码")
    private String  deptCode;


    @ApiModelProperty(name = "leaderName",value = "负责人用户名字")
    private String leaderName;

    @ApiModelProperty(value = "公司车队总人数")
    private Integer totalMember;

}
