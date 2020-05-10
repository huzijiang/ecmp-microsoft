package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/17 8:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarLevelVO {

    private Long carGroupId;
    private String groupName;
    private String level;
    private Long carTypeId;
}
