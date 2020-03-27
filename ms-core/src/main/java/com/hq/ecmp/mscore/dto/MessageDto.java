package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: caobj
 * @Date: 2020-03-04 15:26
 */
@Data
public class MessageDto {

    /**
     * 消息类型(1:申请通知,2:审批通知,3派车通知、4行程中通知,5改派通知)乘客
     * 消息类型(1:新任务通知,2:派车通知,3改派通知、4任务取消通知,5任务提示通知)司机
     */
    private String messageType;
    private String messageTypeStr;
    /**
     * 消息通知数量
     */
    private Integer messageCount;
    /**
     * 通知id
     */
    private Long messageId;
    private Long id;
    private String useCarMode;
    private Integer configType;

    public MessageDto() {
    }

    public MessageDto(Long messageId,String messageType, String messageTypeStr, Integer messageCount) {
        this.messageType = messageType;
        this.messageTypeStr = messageTypeStr;
        this.messageCount = messageCount;
        this.messageId = messageId;
    }
}
