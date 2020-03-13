package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.mscore.dto.Page;

import lombok.Data;

@Data
public class RegimeVo {
       
	  Long regimeId;//制度编号
	  
	  String regimeName;//制度名称
	  
	  String sceneName;//场景名称
	  
	  Integer useNum;//使用人数
	  
	  Integer approvalProcess;//  0  不需要审批  不限制    1-需要审批
	  
	  
	  Integer status;// 0-启用   1-禁用
}
