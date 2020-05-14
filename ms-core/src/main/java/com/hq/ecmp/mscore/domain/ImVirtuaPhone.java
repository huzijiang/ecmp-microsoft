package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (ImVirtuaPhone)实体类
 *
 * @author makejava
 * @since 2020-05-12 20:27:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImVirtuaPhone implements Serializable {
    private static final long serialVersionUID = -18902569536078724L;
    
    private Integer id;
    /**
    * 乘客手机号
    */
    private String customPhone;
    /**
    * 司机手机号
    */
    private String driverPhone;
    /**
    * 城市信息
    */
    private String citycode;
    /**
    * 乘客虚拟小号
    */
    private String customVirtuaPhone;
    /**
    * 司机虚拟小号
    */
    private String driverVirtuaPhone;
    /**
    * 虚拟小号id
    */
    private String virtuaBindId;
    
    private Date createTime;

}