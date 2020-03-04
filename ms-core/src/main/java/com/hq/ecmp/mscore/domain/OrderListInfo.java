package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.mscore.vo.AddressVO;
import lombok.Data;

/**
 * @ClassName OrderListInfo
 * @Description TODO 乘客，我的行程订单列表vo模型
 * @Author yj
 * @Date 2020/3/4 10:22
 * @Version 1.0
 */
@Data
public class OrderListInfo {

    //订单编号
    private Long orderId;
    //车型级别名称  公务级//行政级
    private String level;
    //用车方式  自由车 /网约车
    private String useCarMode;
    //订单状态
    private String state;
    //服务类型// 1000预约//2001接机//2002送机//3000包车
    private String serviceType;
    //出发日期
    private String setOutTime;
    //出发地址
    private AddressVO setOutAddress;
    //到达地
    private AddressVO arriveAddress;

}