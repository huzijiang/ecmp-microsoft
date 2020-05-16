package com.hq.ecmp.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author crk
 */
@AllArgsConstructor
@Getter
public enum IMConStant {

    MSG_UNREAD_STATUS(1, "未读消息状态"),
    MSG_READ_STATUS(0, "已读消息状态"),
    MSG_FAIL_READ_STATUS(99, "已失效消息状态"),
    MSG_IDENTITY_MY(0, "代表自己"),
    MSG_IDENTITY_YOUR(1, "代表对方");


    private Integer status;
    private String desp;
}
