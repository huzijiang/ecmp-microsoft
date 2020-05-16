package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.constant.CarConstant;

import lombok.Data;

@Data
public class ServiceTypeCarAuthority {

    String vehicle;//交通工具T001   飞机 T101   火车   T201   汽车  T301   轮渡   T999   其他
    String cityName;//城市名字
    String cityCode;//城市编号

    Integer surplusCount = 0;//剩余服务次数

    String state;//状态   去申请/派车中/约车中/待服务/进行中/待确认/已完成

    Long noteId;

    Long ticketId;//权限ID

    Long regimenId;//制度编号

    String CarType;//用车方式

    String setoutEqualArrive;// Y000-不允许跨域     N111-允许跨域

    String useStatus;//是否被使用  U999 -已使用     U000 -未使用

    Long orderId;//订单编号
    /**
     * 无车驳回原因
     */
    String rejectReason;


    public void parseSurplusCount() {
        if (null != this.useStatus) {
            if (CarConstant.NOT_USER_USE_CAR.equals(this.useStatus)) {
                this.surplusCount++;
            }
        }
    }

}
