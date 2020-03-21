package com.hq.ecmp.mscore.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 用车权限
 * @author cm
 *
 */
@Data
public class CarAuthorityInfo {
		
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
       String carType;//用车方式  自有车or网约车
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
       
       Long  ticketId;//权限Id
       
       Long regimenId;//制度ID
       
       String setoutEqualArrive;// Y000-不允许跨域     N111-允许跨域
       
       String returnIsType;// 公务  T001-去程    T009-返程
       
       String serviceType;//1000-即时用车    2000-预约用车   3000-接机   4000-送机   5000-包车
       
       
       
       
}
