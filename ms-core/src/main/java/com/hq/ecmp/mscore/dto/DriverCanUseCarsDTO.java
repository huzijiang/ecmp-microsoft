package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: ShiXin
 * @Date: 2020-03-04 15:26
 */
@Data
public class DriverCanUseCarsDTO {
    /**
     * 品牌
     */
    private  String  carType;
    /**
     * 车牌号
     */
    private  String  carLicense;
    /**
     * 能源类型
     */
    private  String  powerType;
    /**
     * 资产编号
     */
    private  String  assetTag;
    /**
     * 所属公司
     */
    private  String  deptName;
    /**
     * 所属车队
     */
    private  String  carGroup;
    /**
     * 车辆性质
     */
    private  String  source;
    /**
     * 状态
     */
    private  String  state;



}
