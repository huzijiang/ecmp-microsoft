package com.hq.ecmp.mscore.bo;

import lombok.Data;

/**
 * 城市信息  用于接收 云端查询结果
 * @author cm
 *
 */
@Data
public class CityInfo {
	String  longitude;//经度
	String latitude;//纬度
	String longAddress;//长地址
	String  shortAddress;//短地址
}
