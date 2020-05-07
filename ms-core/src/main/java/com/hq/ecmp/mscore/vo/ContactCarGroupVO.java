package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/5/7 17:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactCarGroupVO {
    @ApiModelProperty(name = "phone",value = "电话")
    private String phone;
    @ApiModelProperty(name = "name",value = "名字（车队名或者调度员名字）")
    private String name;
    @ApiModelProperty(name = "type",value = "类型 0（车队），1（调度员）")
    private Integer type;
}
