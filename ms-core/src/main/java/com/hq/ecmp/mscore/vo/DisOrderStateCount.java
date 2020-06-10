package com.hq.ecmp.mscore.vo;

import com.hq.ecmp.constant.DispatchOrderStateTraceEnum;
import lombok.Data;

/**
 * <p>调度状态统计model</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 10:42
 */
@Data
public class DisOrderStateCount implements Comparable<DisOrderStateCount> {

    /**
     * 状态
     */
    private String state;
    /**
     * 数量
     */
    private Integer count;

    public DisOrderStateCount(){

    }

    public DisOrderStateCount(String state, Integer count) {
        this.state = state;
        this.count = count;
    }

    @Override
    public int compareTo(DisOrderStateCount o) {
        return getOrderBy(this.state) - getOrderBy(o.state);
    }

    public Integer getOrderBy(String state){
        if(state.equals(DispatchOrderStateTraceEnum.WAITINGLIST.getStateName())){
            return 1;
        }else if(state.equals(DispatchOrderStateTraceEnum.ALREADYSENDING.getStateName())){
            return 2;
        }else if(state.equals(DispatchOrderStateTraceEnum.ORDERDENIED.getStateName())){
            return 3;
        }else {
            return 4;
        }
    }
}
