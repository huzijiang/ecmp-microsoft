package com.hq.ecmp.mscore.domain;

import java.util.Date;

import com.hq.ecmp.mscore.dto.Page;

import lombok.Data;

@Data
public class ApplyDispatchQuery  {
        String applyName;//申请人姓名
        
        String applyMobile;//申请人手机号
        
        String cityCode;//用车城市编码
        
        Date optStartDate;//操作时间  开始
        
        Date optEndDate;//操作时间  结束
        
        String useCarMode;//用车方式  自有车-W100     网约车 -W200
        
        String dispatchStatus;//调度状态   T001-待派车/待改派    T002-已过期    T003-已处理/已通过  T004-已驳回
        
        String driverName;//驾驶员姓名
        
        String driverMobile;//驾驶员手机号
        
        
        
       
}
