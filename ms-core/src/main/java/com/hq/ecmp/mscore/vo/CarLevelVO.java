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

    private String groupId;
    private String groupName;

    public CarLevelVO() {
    }

    public CarLevelVO(String groupName, String groupId) {
        this.groupName = groupName;
        this.groupId = groupId;

    }
}
