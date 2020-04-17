package com.hq.ecmp.mscore.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/4/7 21:25
 * @Version 1.0
 */
@ApiModel("驾驶员排班模型")
@Data
public class DriverWorkInfoVo {


   // @ApiModelProperty(value = "排班id")
   // private Long workId;

    private Long driverId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date calendarDate;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String  onDutyRegisteTime;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private String offDutyRegisteTime;
    private String todayItIsOnDuty;



}
