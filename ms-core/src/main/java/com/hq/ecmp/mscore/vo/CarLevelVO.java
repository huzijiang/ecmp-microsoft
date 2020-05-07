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


    private String groupName; //车型级别代码,一个车可以归属为多个级别  P001-公务级  P002-行政级  P003-六座商务
    private Integer groupId;
}
