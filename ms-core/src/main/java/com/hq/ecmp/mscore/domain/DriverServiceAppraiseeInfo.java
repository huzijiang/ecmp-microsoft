package com.hq.ecmp.mscore.domain;

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
    private Integer score;
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

    public DriverServiceAppraiseeInfo(Long orderId) {
    }


    public Long getAppraiseId() {
        return appraiseId;
    }

    public void setAppraiseId(Long appraiseId) {
        this.appraiseId = appraiseId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getCarLicense() {
        return carLicense;
    }

    public void setCarLicense(String carLicense) {
        this.carLicense = carLicense;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}