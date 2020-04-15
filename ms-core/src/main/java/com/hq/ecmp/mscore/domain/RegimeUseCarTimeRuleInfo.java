package com.hq.ecmp.mscore.domain;

import java.util.Date;

import lombok.Data;

@Data
public class RegimeUseCarTimeRuleInfo {

	Long regimenId;

	String ruleAction; // Y000 在时间内 N001 在时间外

	/**
	 * R201-工作日 R202-工作日次日
	 * 
	 * R301-非工作日 R302-非工作日次日
	 * 
	 * R101-周一 R102-周二 R103-周三 R104-周四 R105-周五 R106-周六 R107-周日
	 */
	String ruleKey;

	String startTime;// 时分 08:12

	String endTime;// 时分

	Long createBy;

	Date createTime;

	Long updateBy;

	Date updateTime;

}
