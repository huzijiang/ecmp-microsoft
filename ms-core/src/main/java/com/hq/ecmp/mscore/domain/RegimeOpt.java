package com.hq.ecmp.mscore.domain;

import lombok.Data;

@Data
public class RegimeOpt {
		Long regimeId;
		String optType;//Y000-启用   E000-停用  M100-删除
		Long optUserId;//操作人员ID
}

