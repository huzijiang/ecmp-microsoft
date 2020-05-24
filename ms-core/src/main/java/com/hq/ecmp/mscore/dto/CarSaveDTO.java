package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.vo.DriverVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/14 23:17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarSaveDTO {

    //1.车辆ID
    private Long carId;

    //1.车辆品牌
    private String carType;

    //2.车牌号码
    private String carLicense;

    //3.车型 商务 公务 等
    private Long enterpriseCarTypeId;

    //4.所属组织
    private Long ownerOrgId;


    //5.所属车队
    private Long carGroupId;

    //6.车辆性质 自有 （购买时间） 租赁 （租赁日期 XXX - XXX） 借调 （借调日期 xxx - xxx）
    private String source;  // TODO 新增  车辆来源  S001   购买    s002   租赁  S003   借调

    //购买日期
    private Date buyDate;

    //租赁到期时间
    private Date rentEndDate;
    private Date rentStartDate;   // TODO car_info表缺该字段  租赁开始时间

    private Date borrowStartDate; // TODO car_info缺该字段 借调开始时间
    private Date borrowEndDate;  // TODO car_info缺该字段 借调结束时间

    //7.可用驾驶员
    private List<DriverVO> drivers;

    // --------------  车辆实照信息 -------------------
    //1.资产编号
    private String assetTag;  //TODO 新增

    //2.车辆颜色
    private String carColor;

    //3.能源类型
    private String powerType;     //动力类型:P001   汽油  P002   柴油  P003   电力   P004   混合

    //4.核定载客数
    private Integer seatNum;

    //5.购车价格
    private Double price; //TODO 新增

    //6.车辆购置税
    private Double tax; // TODO 新增

    //7.牌号费
    private Double licensePrice; //TODO 新增

    //8.车辆实照
    private String carImgaeUrl;  //车辆图片上传 多张用 ， 隔开

    //9.行驶证号码
    private String drivingLicense; // TODO 新增

    //10.行驶证有效期  开始时间--结束时间
    private Date drivingLicenseStartDate; // TODO 新增
    private Date drivingLicenseEndDate;  // TODO 新增

    //11.行驶证实照
    private String carDrivingLicenseImagesUrl;

    private String carGroupName; //车队名字（回显展示）
    private String carTypeName; // 车型名字（回显展示）
    //private String powerTypeName; // 燃料类型名字 回显展示）暂不使用
    private String ownerCompanyName; //所属公司名字

    private Long companyId;

    private String  icCard;  //IC卡
    private String  fnNumber;  //档案号
    private Date  registeTime;  //注册时间
    private Date  annualVerificationTime;  //年度审核日期
    private Date  lastMaintainTime;  //最后维修时间

    private String engineNumber; //发动机编号
    private String carNumber;  //车架号

    private String powerTypeName;
}
