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
@ApiModel("云端获取排班初始数据")
@Data
public class CloudWorkIDateVo {


   // @ApiModelProperty(value = "排班id")
   // private Long workId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date calendarDate;
    private String calendarYear;
    private String calendarMonth;
    private String calendarDay;
    private String itIsWork;
    private String workStart;
    private String workEnd;



}
