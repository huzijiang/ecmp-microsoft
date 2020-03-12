package com.hq.ecmp.mscore.domain;

import lombok.Data;

@Data
public class RegimeOpt {
		Long regimeId;
		Integer optType;//0-启用   1-停用  3-删除
		Long optUserId;//操作人员ID
}
