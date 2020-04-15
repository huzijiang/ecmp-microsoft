package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.constant.CarConstant;

import lombok.Data;

@Data
public class UserCarAuthority implements Comparable<UserCarAuthority>{
	
	
     String type;//C001  接机    C009  送机     C222  市内用车
     
     
     String state;//状态   去申请/派车中/约车中/待服务/进行中/待确认/已完成
     
     
     String authorityUse;//U999 -已使用      U000  -未使用
     
     Integer pickupCount=0;//接机剩余次数
     
     Integer sendCount=0;//送机剩余次数
     
    String cityName;
    
    Long orderId;//订单编号   
     
    Long ticketId;//用车权限ID
    
    Long regimenId;//制度ID
    
    String carType;//用车方式
    
    String setoutEqualArrive;// Y000-不允许跨域     N111-允许跨域
    
     /**
      * 获取接机or送机剩余次数
      */
     public void handCount(){
    	 if(CarConstant.NOT_USER_USE_CAR.equals(this.authorityUse)){
    		 switch (this.type) {
			case CarConstant.USE_CAR_AIRPORT_PICKUP:
				this.pickupCount++;
				break;
			case CarConstant.USE_CAR_AIRPORT_DROP_OFF:
				this.sendCount++;
				break;
			default:
				break;
			}
    	 }
    	
     }

    @Override
    public int compareTo(UserCarAuthority o) {
        return orderNum(this.getType())-orderNum(o.getType());
    }
    private int  orderNum(String type){
         if(type.equals(CarConstant.USE_CAR_AIRPORT_PICKUP)){
             return 1;
         }else if(type.equals(CarConstant.CITY_USE_CAR)){
             return 2;
         }else{
             return 3;
         }
    }
}
