package com.hq.ecmp.mscore.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import	java.util.Date;

import java.util.Date;
import java.io.Serializable;

/**
 * (ImWindow)实体类
 *
 * @author makejava
 * @since 2020-05-10 09:40:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImWindow implements Serializable {
    private static final long serialVersionUID = 596926391057042741L;
    
    private Long id;
    /**
    * 接收人类型
    */
    private Integer receiveRoleType;
    /**
    * 接收人ID
    */
    private Long receiveId;
    /**
    * 发送人类型
    */
    private Integer sendRoleType;
    /**
    * 发送人ID
    */
    private Long sendId;
    /**
    * 窗口状态：1启用：2：关闭
    */
    private Integer status;
    
    private Date createTime;




}