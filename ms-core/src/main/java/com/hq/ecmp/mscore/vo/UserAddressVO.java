package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel(description = "用户地址模型")
public class UserAddressVO {

    Map<String, List<AddressVO>> addressInfo;
}
