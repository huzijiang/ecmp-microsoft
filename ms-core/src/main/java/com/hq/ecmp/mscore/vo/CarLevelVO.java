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
public class CarLevelVO {

    private Long carGroupId;
    private String groupName;
    private String level;
    private Long carTypeId;

    public CarLevelVO() {
    }

    public CarLevelVO(String groupName, String level, Long carTypeId) {
        this.groupName = groupName;
        this.level = level;
        this.carTypeId = carTypeId;
    }
}
