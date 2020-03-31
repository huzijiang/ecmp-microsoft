package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.constant.enumerate.CarLockStateEnum;
import com.hq.ecmp.constant.enumerate.CarStateEnum;
import com.hq.ecmp.constant.enumerate.TaskConflictEnum;
import com.hq.ecmp.mscore.domain.CarInfo;
import lombok.Data;

import java.sql.Timestamp;

/**
 * 等待被选择的车辆对象
 *
 * @Author: zj.hu
 * @Date: 2020-03-17 21:57
 */
@Data
public class WaitSelectedCarBo extends CarInfo implements Comparable<WaitSelectedCarBo> {

    /**
     * 车辆分类：
     *
     * A-前后无任务
     * B-车辆仅有前任务
     * C-车辆仅有后任务
     * D-车辆前后都有任务
     */
    private String type;
    /**
     * 优先值 最低 1
     */
    private int priority;

//    /**
//     * 车辆状态
//     * S000    启用中
//     * S001    禁用中
//     * S002    维护中
//     * S003    已到期
//     * S101    被借调
//     */
//    private String state;

    /**
     * 车辆锁定状态
     * 0000   未锁定
     * 1111   已锁定
     */
    private String lockState;

    /**
     * 任务是否冲突，用于自动派单
     * 需要查询 车辆 前后任务  结束 和 开始 时间进行判断,
     * 100 前任务冲突
     * 001 后任务冲突
     * 101 前后任务都冲突
     * 000 无任务冲突
     *
     */
    private String  taskConflict;

    /**
     * 任务流情况
     * 查询车辆是否有前置和 后置任务，仅限当日
     * 000  前后都没有任务
     * 100  前面有任务
     * 001  后面有任务
     * 101  前后都有任务
     *
     */
    private String  taskStream;


    /**
     * 前续 冲突的任务 订单编号
     */
    private Long beforeClashTaskOrderId;
    /**
     * 前续个任务结束时间 这里是不冲突的任务
     */
    private Timestamp beforeTaskEndTime;

    /**
     * 前续个任务订单编号，这里是不冲突的任务
     */
    private Long beforeTaskOrderId;

    /**
     * 后续 冲突的任务 订单编号
     */
    private Long afterClashTaskOrderId;
    /**
     * 后续个任务开始时间 这里是不冲突的任务
     */
    private Timestamp afterTaskBeginTime;

    /**
     * 后续任务订单编号,这里是不冲突的任务
     */
    private Long  afterTaskOrderId;

    /**
     * 车辆级别
     */
    private String level;

    /**
     * 车队名称
     */
    private String carGroupName;
    /**
     *
     * 综合状态： 根据各种状态综合判断
     *
     */
    private String compositeState;



    /**
     * 等于零 则o1=o2
     * 大于零 则o1>o2
     * 小于零 则o1<o2
     */
    @Override
    public int compareTo(WaitSelectedCarBo o) {

        return this.priority - o.priority;
    }

    public void   embellish(){
        if(CarStateEnum.EFFECTIVE.getCode().equals(this.getState())){
            this.compositeState.concat(CarStateEnum.EFFECTIVE.getDesc());
        }
        if(CarStateEnum.NONEFFECTIVE.getCode().equals(this.getState())){
            this.compositeState.concat(CarStateEnum.NONEFFECTIVE.getDesc());
        }
        if(CarStateEnum.MAINTENANCE.getCode().equals(this.getState())){
            this.compositeState.concat(CarStateEnum.MAINTENANCE.getDesc());
        }
        if(CarStateEnum.EXPIRE.getCode().equals(this.getState())){
            this.compositeState.concat(CarStateEnum.EXPIRE.getDesc());
        }
        if(CarStateEnum.WAS_BORROWED.getCode().equals(this.getState())){
            this.compositeState.concat(CarStateEnum.WAS_BORROWED.getDesc());
        }

        if(CarLockStateEnum.UNLOCK.getCode().equals(this.lockState)){
            this.compositeState.concat(CarLockStateEnum.UNLOCK.getDesc());
        }

        if(TaskConflictEnum.BEFORE_TASK_CLASH.getCode().equals(this.getTaskConflict())){
            this.compositeState.concat(TaskConflictEnum.BEFORE_TASK_CLASH.getDesc());
        }
        if(TaskConflictEnum.AFTER_TASK_CLASH.getCode().equals(this.getTaskConflict())){
            this.compositeState.concat(TaskConflictEnum.AFTER_TASK_CLASH.getDesc());
        }
        if(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH.getCode().equals(this.getTaskConflict())){
            this.compositeState.concat(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH.getDesc());
        }
        if(TaskConflictEnum.CONFLICT_FREE.getCode().equals(this.getTaskConflict())){
            this.compositeState.concat(TaskConflictEnum.CONFLICT_FREE.getDesc());
        }

    }


}
