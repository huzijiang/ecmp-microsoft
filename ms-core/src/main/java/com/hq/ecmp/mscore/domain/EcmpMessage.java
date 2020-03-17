package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (EcmpMessage)实体类
 *
 * @author makejava
 * @since 2020-03-13 15:25:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcmpMessage implements Serializable {
    private static final long serialVersionUID = 480753607230255189L;
    
    private Long id;
    /**
    * 对应用户类型（1.乘客，2.司机，3.调度员。4，审批员）
    */
    private Integer configType;
    /**
    * 对应用户类型id
    */
    private Long ecmpId;
    private Long categoryId;
    /**
    * 消息类型
T001-业务消息
T002-
T003
T004
    */
    private String type;
    /**
    * 消息状态  分已读 和 未读
0000-已读
1111-未读
    */
    private String status;

    private String content;
    /**
    * 消息类别，随业务自行添加
M001  申请通知
M002  审批通知
M003  调度通知
M004  订单改派
M005  订单取消
M999  其他
    */
    private String category;
    /**
    * 事项处理跳转链接地址，
需要密切联系业务调整规则

大部分应该是跳转到  事项处理的列表页

单个具体事项出题 请带上 业务的主键ID，
    */
    private String url;
    
    private Long createBy;
    
    private Date createTime;
    
    private Long updateBy;
    
    private Date updateTime;

    public EcmpMessage(Integer configType, String type, String status) {
        this.configType = configType;
        this.type = type;
        this.status = status;
    }

    public EcmpMessage(Integer configType, String status,Long ecmpId) {
        this.configType = configType;
        this.status = status;
        this.ecmpId = ecmpId;
    }
}