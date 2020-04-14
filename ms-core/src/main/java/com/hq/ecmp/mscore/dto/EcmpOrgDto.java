package com.hq.ecmp.mscore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import com.hq.ecmp.mscore.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("部门管理dto(返回前端)")
public class EcmpOrgDto {

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

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "负责人")
    private String leader;
    @ApiModelProperty(value = "负责人姓名")
    private String leaderName;
    @ApiModelProperty(value = "部门主管")
    private List<UserVO> leaderUsers;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "部门状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty(value = "创建者")
    private String createBy;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新者")
    private String updateBy;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "新增临时字段  分/子公司人数")
    private String numOfSub;

    @ApiModelProperty(value = "新增临时字段  下级公司数")
    private String numOfSonCom;


    @ApiModelProperty(value = "新增临时字段  上级公司")
    private String supComName;

    @ApiModelProperty(value = "新增临时字段  下级部门数")
    private String numOfSonDept;

    /*新增临时字段 */
    @ApiModelProperty(value = "组织下级")
    private List<EcmpOrgDto> deptList;

}
