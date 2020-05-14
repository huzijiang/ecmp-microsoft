package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crk
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IMWindowVO {
    private Long id;
    /**
     * 接收人类型
     */
    private Integer receiveRoleType;
    /**
     * 接收人ID
     */
    private Integer receiveId;
    /**
     * 发送人类型
     */
    private Integer sendRoleType;
    /**
     * 发送人ID
     */
    private Integer sendId;
    /**
     * 窗口状态：1启用：2：关闭
     */
    private Integer status;

    /**
     * 接收人姓名
     */
    private String receiveName;

    /**
     * 未读消息
     */
    private Integer unReadCount;

}
