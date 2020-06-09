package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * <p>调度状态统计model</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 10:42
 */
@Data
public class DisOrderStateCount {

    /**
     * 状态
     */
    private String state;
    /**
     * 数量
     */
    private Integer count;
}
