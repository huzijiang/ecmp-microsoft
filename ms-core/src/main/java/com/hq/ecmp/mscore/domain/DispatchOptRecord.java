package com.hq.ecmp.mscore.domain;

import java.util.Date;

import lombok.Data;

@Data
public class DispatchOptRecord {

	String rejectReason;// 驳回原因 改派驳回时存在

	String useCarModel;// 用车方式

	String driverName;// 驾驶员

	String driverMobile;// 驾驶员电话

	String carType;// 车型

	String carLicense;// 车牌号

	String dispatchName;// 调度员名字

	String dispatchMobile;// 调度员电话号码

	Date dispatchDate;// 调度时间
}
