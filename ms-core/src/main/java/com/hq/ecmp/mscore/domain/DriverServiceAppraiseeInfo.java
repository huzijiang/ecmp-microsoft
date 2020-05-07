package com.hq.ecmp.mscore.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 驾驶员-服务评价表

针对自使用自有车辆的订单，可以记录评价详细内容
针对 网约车 订单，记录其星值分数
(DriverServiceAppraiseeInfo)实体类
 *
 * @author makejava
 * @since 2020-03-07 15:56:25
 */
@Data
public class DriverServiceAppraiseeInfo implements Serializable {
    private static final long serialVersionUID = 359913880982924174L;
    /**
    * 主键
    */
    private Long appraiseId;
    /**
    * 订单id
    */
    private Long orderId;
    /**
    * 选项
Y001 道路熟悉
Y002 驾驶平顺
Y003 车内清新
Y004 非常礼貌
Y005 着装整齐

N001  道路不熟
N002  驾驶不稳
N003  车内异味
N004  开车打电话
N005  着装不整
    */
    private String item;
    /**
    * 得分
0-10分  对应 5颗星
比如：3分 代表1颗半星

    */
    private Double score;
    /**
    * 评价内容
    */
    private String content;

    private Long driverId;

    private Long carId;

    private String carLicense;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    public DriverServiceAppraiseeInfo() {
    }

    public DriverServiceAppraiseeInfo(Long driverId) {
        this.driverId = driverId;
    }

}
