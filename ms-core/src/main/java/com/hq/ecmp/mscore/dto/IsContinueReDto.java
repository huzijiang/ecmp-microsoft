package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.domain.OrderDriverListInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName IsContinueReDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/11 16:41
 * @Version 1.0
 */
@Data
@ApiModel(value = "司机是否继续用车的返回model")
public class IsContinueReDto {

    @ApiModelProperty(value = "是否还车",notes = "1:是 ，2:否")
    private int  isCallBack;

    @ApiModelProperty(value = "此车接下来是否被使用")
    private  int  isCarUse;

    @ApiModelProperty(value = "车被使用的时间",notes = "车有被使用时，使用此字段,格式 yyyy-mm-dd hh24:mi")
    private String  useDateTime;

    @ApiModelProperty(value = "司机的下一个任务卡片信息",notes = "如果不需要还车时，使用此数据")
    private OrderDriverListInfo nextTaskDetailWithDriver;
}
