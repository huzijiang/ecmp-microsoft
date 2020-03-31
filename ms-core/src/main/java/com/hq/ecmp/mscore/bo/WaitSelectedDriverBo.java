package com.hq.ecmp.mscore.bo;

import com.hq.ecmp.constant.enumerate.CarLockStateEnum;
import com.hq.ecmp.constant.enumerate.CarStateEnum;
import com.hq.ecmp.constant.enumerate.DriverStateEnum;
import com.hq.ecmp.constant.enumerate.TaskConflictEnum;
import com.hq.ecmp.mscore.domain.DriverInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * 等待被选中的司机对象
 *
 * @Author: zj.hu
 * @Date: 2020-03-17 21:56
 */
@Data
public class WaitSelectedDriverBo extends DriverInfo implements Comparable<WaitSelectedDriverBo> {
    /**
     * 人员任务分类：
     *
     * A-前后无任务
     * B-人员仅有前任务
     * C-人员仅有后任务
     * D-人员前后都有任务
     */
    private String type;

    /**
     * 优先值 最低 1
     */
    private int priority;

    /**
     * 司机状态
     * 0000 正常上班、
     * 1111 其他原因不能正常上班
     */
    private String state;

    /**
     * 司机锁定状态
     * 0000   未锁定
     * 1111   已锁定
     */
    private String lockState;

    /**
     * 任务是否冲突，用于自动派单
     * 需要查询 司机 前后任务  结束 和 开始 时间进行判断,
     * '100' 前任务冲突
     * '001' 后任务冲突
     * '101' 前后任务都冲突
     * '000' 无任务冲突
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
     * 工作日
     */
    private Date calendarDate;

    /**
     * 工作状态
     */
    private String leaveStatus;

    /**
     * 车辆编号
     */
    private Long carId;
    /**
     * 归属车队
     */
    private Long carGroupId;

    /**
     * 综合状态： 根据各种状态综合判断
     */
    private String compositeState;



    @Override
    public int compareTo(WaitSelectedDriverBo o) {

        return this.priority - o.priority;
    }

    public void   embellish(){

        if(DriverStateEnum.EFFECTIVE.getCode().equals(this.getState())){
            this.compositeState.concat(DriverStateEnum.EFFECTIVE.getDesc());
        }
        if(DriverStateEnum.WAIT_CHECK.getCode().equals(this.getState())){
            this.compositeState.concat(DriverStateEnum.WAIT_CHECK.getDesc());
        }
        if(DriverStateEnum.DIMISSION.getCode().equals(this.getState())){
            this.compositeState.concat(DriverStateEnum.DIMISSION.getDesc());
        }

        if(TaskConflictEnum.BEFORE_TASK_CLASH.getCode().equals(this.taskConflict)){
            this.compositeState.concat(TaskConflictEnum.BEFORE_TASK_CLASH.getDesc());
        }
        if(TaskConflictEnum.AFTER_TASK_CLASH.getCode().equals(this.taskConflict)){
            this.compositeState.concat(TaskConflictEnum.AFTER_TASK_CLASH.getDesc());
        }
        if(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH.getCode().equals(this.taskConflict)){
            this.compositeState.concat(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH.getDesc());
        }
        if(TaskConflictEnum.CONFLICT_FREE.getCode().equals(this.taskConflict)){
            this.compositeState.concat(TaskConflictEnum.CONFLICT_FREE.getDesc());
        }
    }



}
