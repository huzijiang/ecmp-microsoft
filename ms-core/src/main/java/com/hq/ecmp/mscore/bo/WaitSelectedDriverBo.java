package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.mscore.domain.DriverInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *等待被选中的司机对象
 * @Author: zj.hu
 * @Date: 2020-03-17 21:56
 */
@Data
public class WaitSelectedDriverBo extends DriverInfo implements  Comparable<WaitSelectedDriverBo> {

    /**
     * 优先值 最低 1
     */
    @ApiModelProperty(name="priority",value="优先值")
    private int priority;



    @Override
    public int compareTo(WaitSelectedDriverBo o) {
        return this.priority-o.priority;
    }
}
