package com.hq.ecmp.mscore.domain;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (EcmpNoticeMapping)实体类
 *
 * @author makejava
 * @since 2020-03-11 18:42:33
 */
@Data
public class EcmpNoticeMapping implements Serializable {
    private static final long serialVersionUID = -37548739821706339L;
    
    private Long id;
    
    private Long noticeId;
    /**
    * 类型：1.全部用户，2.角色，3.部门
    */
    private String configType;
    /**
    * 对应id
    */
    private Long bucId;
    /**
    * 状态，0：有效，1：无效
    */
    private String status;
    
    private Date createTime;
    
    private Date updateTime;

}