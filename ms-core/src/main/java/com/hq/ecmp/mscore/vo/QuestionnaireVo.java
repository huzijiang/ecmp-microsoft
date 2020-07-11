package com.hq.ecmp.mscore.vo;

import com.hq.core.aspectj.lang.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: chao.zhang
 * @Date: 2020/6/11 13:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireVo {

    /** id */
    private Long id;

    /** 司机id */
    private Long driverId;

    /** 车辆id */
    private Long carId;

    /** 星级评分 */
    private Integer score;

    /** 是否按时 */
    private String itIsOnTime;

    /** 是否主动开门，拿行李 */
    private String itIsHelp;

    /** 是否不接私人电话，不主动攀谈 */
    private String itIsCalm;

    /** 是否熟悉路况 */
    private String itIsKnowTraffic;

    /** 是否平稳驾驶 */
    private String itIsSmooth;

    /** 是否干净 */
    private String itIsClean;

    /** 是否有异味 */
    private String itIsOdor;

    /** 用车时间 */
    private Date useCarTime;

    /** 联系电话 */
    private String phone;

    private Date createTime; //评价时间

    private String driverName;  //驾驶员名字

    private String carLicense; //车牌号

    private String orderNum; //订单编号

}
