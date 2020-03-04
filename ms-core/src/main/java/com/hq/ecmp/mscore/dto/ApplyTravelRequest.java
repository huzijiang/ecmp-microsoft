package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.vo.TravelPickupCity;
import com.hq.ecmp.mscore.vo.TravelRequest;
import com.hq.ecmp.mscore.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

/**
 * @author xueyong
 * @date 2020/1/6
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "申请单添加对象")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  ApplyTravelRequest {

    /**
     * 用车类型 eg 自有/网约车
     */
    @ApiModelProperty(name = "useType", value = "用车类型", required = true, position = 1, example = "自有/网约车")
    private String useType;

    /**
     * 行程信息，集合
     */
    @ApiModelProperty(name = "travelVOS", value = "行程信息", required = true, position = 2)
    private List<TravelRequest> travelRequests;

    /**
     * 出差总天数
     */
    @ApiModelProperty(name = "travelCount", value = "出差总天数", required = true, position = 3)
    private Integer travelCount;

    /**
     * 申请人
     */
    @ApiModelProperty(name = "applyUser", value = "申请人", required = false, position = 4)
    private UserVO applyUser;
    /**
     * 乘车人
     */
    @ApiModelProperty(name = "passenger", value = "乘车人", required = true, position = 5)
    private UserVO passenger;

    /**
     * 是否往返
     */
    @ApiModelProperty(name = "isGoBack", value = "是否往返", required = false, position = 6)
    private String isGoBack;

    /**
     * 成本中心,部门ID
     */
    @ApiModelProperty(name = "costCenter", value = "成本中心", required = false, position = 7)
    private String costCenter;
    /**
     * 项目编号
     */
    @ApiModelProperty(name = "projectNumber", value = "项目编号", required = false, position = 8)
    private String projectNumber;

    /**
     * 申请原因
     */
    @ApiModelProperty(name = "reason", value = "申请原因", required = true, position = 9)
    private String reason;

    /**
     * 用车制度
     */
    @ApiModelProperty(name = "regimenId", value = "用车制度id", required = true, position = 10)
    private Integer regimenId;  //TODO 新增

    /**
     * 申请类型 eg 公务、差旅
     */
    @ApiModelProperty(name = "applyType", value = "申请类型", required = true, position = 12, example = "公务")
    private String applyType;   //TODO 新增   Integer 改字符串

    /**
     * 审批人
     */
    @ApiModelProperty(name = "approvers", value = "审批人", required = false, position = 13)
    private List<UserVO> approvers;  // TODO 新增

    /**
     * 出差市内用车城市
     */
    @ApiModelProperty(name = "travelCitiesStr", value = "市内用车城市", required = false, position = 13,example = "北京、上海")
    private String travelCitiesStr;  // TODO 新增

    /**
     * 接送机/站服务总次数
     */
    @ApiModelProperty(name = "pickupTimes", value = "接送机/站服务总次数", required = false, position = 14)
    private Integer pickupTimes;  // TODO 新增

    /**
     * 接送机/站服务城市及接送情況   [{"city":"北京",“pickup”:0，"dropOff":0},{"city":"上海",“pickup”:1，"dropOff":0}]
     */
    @ApiModelProperty(name = "travelPickupCity", value = "接送机/站服务城市数组", required = false, position = 15)
    private List<TravelPickupCity> travelPickupCity;  // TODO 新增

    /**
     * 標題   北京-长沙、上海、南京
     */
    //  @ApiModelProperty(name = "title", value = "行程路经城市", required = false, position = 16)
    //  private String title;  // TODO 新增

    /**
     * 第一个行程开始时间
     */
    @ApiModelProperty(name = "startDate", value = "行程最小开始时间", required = false, position = 17)
    private Date startDate;  // TODO 新增

    /**
     * 最后一个行程结束时间
     */
    @ApiModelProperty(name = "endDate", value = "行程最后结束时间", required = false, position = 18)
    private Date endDate;  // TODO 新增


}
