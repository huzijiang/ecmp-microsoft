package com.hq.ecmp.ms.api.dto.journey;

import lombok.Data;

/**
 * 用车申请 申请人模糊查询参数
 *
 * @author Chenkp
 * @date 2020-07-23 12:09
 */
@Data
public class JourneyNameDto {

    /**
     * 登录人部门ID
     */
    private Long deptId;

    /**
     * 申请人名称
     */
    private String name;

}
