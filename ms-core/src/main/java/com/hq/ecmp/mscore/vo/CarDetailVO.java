package com.hq.ecmp.mscore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/16 13:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDetailVO {

    //1.车辆品牌
    private String carType;

    //2.车牌号码
    private String carLicense;

    //3.车辆颜色
    private String carColor;

    //4.能源类型
    private String powerType;     //动力类型:P001   汽油  P002   柴油  P003   电力   P004   混合

    //5.核定载客数
    private Integer seatNum;

    //6.所属公司
    private String ownerOrg;

    //7.所属部门
    private String deptName;

    //8.车辆性质 自有 （购买时间） 租赁 （租赁日期 XXX - XXX） 借调 （借调日期 xxx - xxx）
    private String source;


    //9.购买日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date buyDate;

    //10.可用驾驶员人数
    private List<DriverVO> drivers;
    private Integer driverNum;

    // -------------------  其他信息 ---------------------

    //1.资产编号
    private String assetTag;

    //2.购车价格
    private Double price;

    //3.车辆购置税
    private Double tax;

    //4.牌号费
    private Double licensePrice;

    //5.车辆实照
    private String carImgaeUrl;

    //6.行驶证号码

    //7.行驶证有效期 xxx -- xxx
    private String drivingLicenseStartDate;

    private String drivingLicenseEndDate;

    //8.行驶证实照
    private String carDrivingLicenseImagesUrl;

    //.车辆id
    @ApiModelProperty(name = "carId", value = "车辆id")
    private Long carId;




}
