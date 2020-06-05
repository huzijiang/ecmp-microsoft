package com.hq.ecmp.mscore.dto.dispatch;

import lombok.Data;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/6/4 11:26
 * @Version 1.0
 */
@Data
public class DispatchCarGroupDto {

    /**
     * 车队id
     */
    private  Long carGroupId;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 城市编码
     */
    private String city;
    /**
     * 是否是外部车队
     */
    private String itIsInner;
}
