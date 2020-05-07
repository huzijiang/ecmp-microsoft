package com.hq.ecmp.mscore.domain;

import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (UserAcceptOrderAccountEmailInfo)实体类
 *
 * @author makejava
 * @since 2020-03-12 12:02:16
 */
@Data
public class UserAcceptOrderAccountEmailInfo implements Serializable {
    private static final long serialVersionUID = 285675296178088629L;

    private Integer id;

    private Long userId;

    private String email;

    private String state;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

}
