package com.hq.ecmp.constant.enumerate;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: zj.hu
 * @Date: 2020-03-30 11:40
 */
public enum TaskConflictEnum {
    /**
     * 可用
     */
    BEFORE_TASK_CLASH("100","前序任务冲突"),
    /**
     * 等待审核
     */
    AFTER_TASK_CLASH("001","后序任务冲突"),
    /**
     * 司机已经离职或者失效
     */
    BEFORE_AND_AFTER_TASK_CLASH("101","前序后序任务皆冲突"),

    /**
     * 司机已经离职或者失效
     */
    CONFLICT_FREE("000","无任务冲突");

    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String desc;

    TaskConflictEnum(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

}
