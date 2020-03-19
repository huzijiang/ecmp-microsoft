package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/18 12:11
 */
@Data
public class SubGroupListDTO {

    private Long carGroupId;

    //车队作为一个部门的部门id
    private Long deptId;
}
