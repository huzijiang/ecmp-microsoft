package com.hq.ecmp.mscore.domain;

import java.util.Date;

import lombok.Data;

@Data
public class RegimeUseCarCityRuleInfo {
            
	Long useCarCityRuleId;
	
	Long regimenId;
	
	String ruleAction;//Y000    在地点内      N001   在地点外
	
	String cityCode;//城市编码
	
	Long createBy;
	
	Date createTime;
	
	Long updateBy;
	
	Date updateTime;
	
	public RegimeUseCarCityRuleInfo(){};
	
	public RegimeUseCarCityRuleInfo(Long regimenId,String ruleAction,String cityCode,Long createBy,Date createTime){
		this.regimenId=regimenId;
		this.ruleAction=ruleAction;
		this.cityCode=cityCode;
		this.createBy=createBy;
		this.createTime=createTime;
	}
	
	
	      
}
