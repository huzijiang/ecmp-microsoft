package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
/**
*   @author yj
*   @Description  //TODO 
*   @Date 10:21 2020/5/11
*   @Param  
*   @return
**/
@Data
public class DispatchOrderInfo {
	
	Long orderId;
	/**
	 * //状态   前端状态  D001,自有车已改派,
	 *     D002,网约车已改派,
	 *     D003,改派申请已驳回,
	 *     D004,派车申请已驳回,
	 *     D005,自有车已派车,
	 *     D006,网约车已派车,
	 *     D007,改派已过期,
	 *     D008,派车已过期;
	 */
	String state;
	/**
	 * //用车类型   公务用车-A001   差旅用车-A002
	 */
	String applyType;

	/**
	 * //服务类型  接机  送机  市内用车
	 */
	String serverType;
	/**
	 * //用车城市
	 */
	String useCarCity;

	/**
	 * //用车城市编号
 	 */
	String useCarCityCode;
	/**
	 * //申请人
	 */
	String applyUserName;
	/**
	 * //申请人手机号
	 */
	String applyUserTel;
	/**
	 * //乘车人
	 */
	String useCarUserName;
	/**
	 * //上车地点
	 */
	String startSite;
	/**
	 * //下车地点
	 */
	String endSite;
	/**
	 * //用车时间
	 */
	Date useCarDate;
	/**
	 * //预计结束时间
	 */
	Date endDate;
	/**
	 * //是否往返   Y000-是        N444 -否
	 */
	String itIsReturn;
	/**
	 * //等待时间  分钟
	 */
	Long waitMinute;
	/**
	 * //自有车- W100   网约车  -W200    多个以"、"分隔
	 */
	String useCarMode;
	/**
	 * 制度id
	 */
	Long regimenId;
	/**
	 * //上车点城市编号
	 */
	String cityId;
	/**
	 * //改派申请理由
	 */
	String reassignmentApplyReason;
	/**
	 * //调度单审批通过时间    调度单
	 */
	Date auditSuccessDate;
	/**
	 * //申请改派时间     改派单
	 */
	Date applyReassignmentDate;
	/**
	 * //用作排序的时间
	 */
	Date updateDate;
	/**
	 * //用车申请单通过的时间
	 */
	Date applyPassDate;
	/**
	 * //同行人
	 */
	List<String> peerUserList;

	/**
	 * //车型
	 */
	String carType;
	/**
	 * //车牌号
	 */
	String carLicense;
	/**
	 * //驾驶员名字
	 */
	String driverName;
	/**
	 * //驾驶员电话号码
	 */
	String driverTel;
	/**
	 * //调度完成后的用车车型
	 */
	String demandCarLevel;

	/**
	 * //申请改派的驾驶员信息  及改派状态
	 */
	DispatchDriverInfo DispatchDriverInfo;
	/**
	 * //派车信息
	 */
	List<SendCarInfo> sendCarInfoList;
	/**
	 * //预计等待时长  毫秒
	 */
	String waitTimeLong;
	/**
	 * //行程编号
	 */
	Long journeyId;
	/**
	 * //行程节点编号
	 */
	Long nodeId;
	/**
	 * //申请单编号
	 */
	Long applyId;
	/**
	 * //订单生成时间
	 */
	Date createTime;
	/**
	 * 申请人的末级公司
	 */
	String companyName;
	/**
	 * 申请人的末级部门
	 */
	String deptName;
	/**
	 * 用车场景名称
	 */
	String userCarScene;
	/**
	 * 用车制度名称
	 */
	String userCarRegime;

    /**
     * 同行人数量
     */
	Integer peerNum;
    /**
     * 操作时间
     */
	Date opDate;
    /**
     * 改派成功前的驾驶员姓名
     */
	String oldDriverName;

    /**
     * 改派成功前的汽车车型
     */
    String oldCarType;
    /**
     * 改派成功前的汽车车牌号
     */
    String oldCarLicense;
	/**
	 * 驳回原因
	 */
	String rejectReason;
	/**
	 *操作权限  0：有权限  1:没有权限
	 */
	String  operationPermission;

	/**
	 * 申请原因
	 */
	@ApiModelProperty(value = "申请原因")
	String reason;

}
