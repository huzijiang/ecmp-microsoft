package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("角色dto")
public class EcmpRoleDto {
    @ApiModelProperty(value="角色ID")
    private Long roleId;

    @ApiModelProperty(value="角色名称")
    private String roleName;

    @ApiModelProperty(value="角色权限字符串")
    private String roleKey;

    @ApiModelProperty(value="显示顺序")
    private Integer roleSort;

    @ApiModelProperty(value="数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private String dataScope;

    @ApiModelProperty(value="角色状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value="删除标志（0代表存在 2代表删除）")
    private String delFlag;
}
