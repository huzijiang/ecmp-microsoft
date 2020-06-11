package com.hq.ecmp.mscore.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用车权限
 *
 * @author cm
 */
@Data
public class CarAuthorityInfo {


    Boolean dispatchOrder;//是否走调度   ture-走调度  false-不走调度
    String planBeginCityCode;
    String planEndCityCode;
    Long journeyId;//行程编号
    String type;//差旅/公务
    List<String> cityName;
    Date startDate;
    Date endDate;
    Integer joinCount;//接机次数
    Integer giveCount;//送机次数
    Integer cityCount;//出差城市用车次数

    String applyName;//公务出差理由
    Date useDate;//公务用车时间
    String carType;//用车方式  自有车or网约车   单个    用做前端判断跳转自有车还是网约车页面用
    String canUseCarMode;//制度里面配置的可用车方式   多个  用做前端显示用

    /* 状态       一下为前端状态
     * 约车中 -S200（网约车）
     * 派车中 -S100(包含了待派单  包含了自有车的)
     * /去约车-S101
     * /去申请-S000
     * /待服务-S299(包含了数据库中的已派车 准备服务)
      /进行中-S616
       *
    * /已完成-S699(包含了服务结束和订单关闭)
    * 待确认-S960  (看后管是否有对自有车或网约车订单确认辅助配置)
    * 已过期 -S970
    * */
    String status;

    Long orderId;//订单编号

    Long ticketId;//权限Id

    Long regimenId;//制度ID

    String setoutEqualArrive;// Y000-不允许跨域     N111-允许跨域

    String returnIsType;// 公务  T001-去程    T009-返程

    String endAddress;//公务  目的地

    String serviceType;//1000-即时用车    2000-预约用车   3000-接机   4000-送机   5000-包车

    String cityCode;//用车城市编码

    /**
     * 申请单id
     */
    Long applyId;
    /**
     * 无车驳回原因
     */
    String rejectReason;
    /**
     * 用车人姓名
     */
    String passengerName;
    /**
     * 用车热人手机号
     */
    String userMobile;
    /**
     * 用车时间
     */
    String useTime;
    /**
     * 是否自驾
     */
    String itIsSelfDriver;

}
