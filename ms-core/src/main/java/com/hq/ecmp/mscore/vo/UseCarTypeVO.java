package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/17 8:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseCarTypeVO {

    private Long applyId;
    /**企业车型*/
    private String enterpriseCarType;
    /** 网约车车型*/
    private String rideHileCarType;
    /** 城市code */
    private String cityCode;
    /** 接送机自由车可用车型*/
    private String shuttleOwnerCarType;

    /** 接送机网约车可用车型 */
    private String shuttleOnlineCarType;

    /** 自由车可用车型 */
    private String ownerCarType;

    /** 网约车可用车型 */
    private String onlineCarType;


}
