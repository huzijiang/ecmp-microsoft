package com.hq.ecmp.mscore.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (ImMessage)实体类
 *
 * @author makejava
 * @since 2020-05-07 10:11:00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImMessage implements Serializable {
    private static final long serialVersionUID = 609594490330936880L;
    
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
    * 消息状态：1：生效，0：未生效：
    */
    private Integer status;
    /**
    * 消息内容
    */
    private String centext;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer identity;



}