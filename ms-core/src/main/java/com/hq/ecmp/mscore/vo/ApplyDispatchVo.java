package com.hq.ecmp.mscore.vo;

import java.util.Date;

import com.github.pagehelper.util.StringUtil;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.util.DateFormatUtils;

import lombok.Data;

@Data
public class ApplyDispatchVo {
	Long orderId;
	
	Long journeyId;
	
	Long nodeId;

	String applyUserName;

	String applyUserMobile;

	String useCarUser;// 乘车人

	Integer peerUserNum;// 同行人人数

	Date startDate;// 开始时间

	Date endDate;// 结束时间

	String setOutAderess;// 上车地址

	String arriveAdress;// 下车地址

	String cityName;// 用车城市
	
	String state;
	
	String itIsReturn;//是否往返

	/**
	 * 
	 * 1000预约 2001接机 2002送机 3000包车
	 */
	String serviceType;// 服务类型
	
	String dispatchStatus;//调度状态   T001-待派车/待改派    T002-已过期    T003-已处理/已通过  T004-已驳回
	
	Date optDate;//操作时间
	
	
	
	public void parseOrderStartAndEndSiteAndTime(DispatchOrderInfo dispatchOrderInfo){
		this.startDate=dispatchOrderInfo.getUseCarDate();
		this.setOutAderess=dispatchOrderInfo.getStartSite();
		this.endDate=dispatchOrderInfo.getEndDate();
		this.arriveAdress=dispatchOrderInfo.getEndSite();
	}
	
	public void parseApplyDispatchStatus(){
		if(StringUtil.isNotEmpty(this.state)){
			if(OrderState.SENDINGCARS.equals(this.state)){
				if(null !=startDate && DateFormatUtils.beforeCurrentDate(this.startDate)){
					//状态处于待派车  但是当前时间已经过了用车开始时间  则状态为已失效
					this.dispatchStatus="T002";
				}else {
					this.dispatchStatus="T001";
				}
			}else {
				this.dispatchStatus="T003";
			}
		}
		
	}
	
	
	public void parseReassignmentDispatchStatus(){
		if(StringUtil.isNotEmpty(this.state)){
			if(OrderState.REASSIGNREJECT.equals(this.state)){
				this.dispatchStatus="T004";
				return;
			}
			if(OrderState.APPLYREASSIGN.equals(this.state)){
				if(null !=startDate && DateFormatUtils.beforeCurrentDate(this.startDate)){
					//状态处于待改派 但是当前时间已经过了用车开始时间  则状态为已过期
					this.dispatchStatus="T002";
				}else {
					this.dispatchStatus="T001";
				}
				return;
			}
			this.dispatchStatus="T003";
		}
	}
}
