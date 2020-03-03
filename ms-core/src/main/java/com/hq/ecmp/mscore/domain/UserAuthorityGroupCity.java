package com.hq.ecmp.mscore.domain;

import java.util.List;

import lombok.Data;

@Data
public class UserAuthorityGroupCity {
	String cityName;
	
	List<UserCarAuthority> userCarAuthorityList;
}
