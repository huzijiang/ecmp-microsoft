package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.mscore.domain.CarInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 等待被选择的车辆对象
 * @Author: zj.hu
 * @Date: 2020-03-17 21:57
 */
@Data
public class WaitSelectedCarBo extends CarInfo implements Comparable<WaitSelectedCarBo> {


    /**
     * 优先值 最低 1
     */
    @ApiModelProperty(name="priority",value="优先值")
    private int priority;

    /**
     *等于零 则o1=o2
     *大于零 则o1>o2
     *小于零 则o1<o2
     */
    @Override
    public int compareTo(WaitSelectedCarBo o) {

        return this.priority-o.priority;
    }

}
