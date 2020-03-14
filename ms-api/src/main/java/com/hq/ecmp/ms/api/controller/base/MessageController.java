package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.MsgStatusConstant;
import com.hq.ecmp.constant.MsgTypeConstant;
import com.hq.ecmp.constant.MsgUserConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.EcmpMessage;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息通知模块
 * @author cm
 *
 */
@RestController
@RequestMapping("/message")
public class MessageController {
	@Autowired
	private ISceneInfoService sceneInfoService;
	@Autowired
    private TokenService tokenService;
	@Autowired
    private IApplyInfoService applyInfoService;
	@Autowired
	private IJourneyInfoService journeyInfoService;
	@Autowired
	private IEcmpUserService userService;
	@Autowired
	private IOrderInfoService orderInfoService;
	@Autowired
	private IOrderStateTraceInfoService orderStateTraceInfoService;
	@Autowired
	private IApplyApproveResultInfoService approveResultInfoService;
	@Autowired
	private IDriverInfoService driverInfoService;

	@Autowired
    private EcmpMessageService ecmpMessageService;
	
	/**
	 * 获取首页轮播正在进行中流程通知(乘客端)
	 * @param
	 * @return
	 */
	@ApiOperation(value = "getRunMessageForPassenger", notes = "获取首页轮播正在进行中流程通知", httpMethod ="GET")
	@GetMapping("/getRunMessageForPassenger")
	public ApiResponse<List<MessageDto>> getRunMessageForPassenger() {
		//获取登录用户
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        List<MessageDto> list=new ArrayList<>();
		//当前是申请人(申请通知/行程通知/改派通知)
		MessageDto applyList= applyInfoService.getApplyMessage(loginUser.getUser().getUserId());
		MessageDto jouMessage=journeyInfoService.getJourneyMessage(loginUser.getUser().getUserId());
		MessageDto traceMessage=orderStateTraceInfoService.getTraceMessage(loginUser.getUser().getUserId(),false,null);
		//如果当前登录人是审批人(审批通知) TODO
		MessageDto approveMassage=approveResultInfoService.getApproveMessage(loginUser.getUser().getUserId());
		//当前登录人为车队管理员(派车通知)
		EcmpUser ecmpUser = userService.selectEcmpUserById(loginUser.getUser().getUserId());
		if (ecmpUser!=null&& "1".equals(ecmpUser.getItIsDispatcher())){
			String states="S100,S000";//订单状态
			MessageDto orderMessage = orderInfoService.getOrderMessage(loginUser.getUser().getUserId(),states,null);
			list.add(orderMessage);
		}
		if (applyList!=null){
			list.add(applyList);
		}
		if (jouMessage!=null){
			list.add(jouMessage);
		}
		if (traceMessage!=null){
			list.add(traceMessage);
		}
		if (approveMassage!=null){
			list.add(approveMassage);
		}
		return ApiResponse.success(list);
	}

	/**
	 * 获取首页轮播正在进行中流程通知(司机端)
	 * @param
	 * @return
	 */
	@ApiOperation(value = "getRunMessageForDrive", notes = "获取首页轮播正在进行中流程通知", httpMethod ="GET")
	@GetMapping("/getRunMessageForDrive")
	public ApiResponse<List<MessageDto>> getRunMessageForDrive() {
		//获取登录用户
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		List<MessageDto> list = new ArrayList<>();
		EcmpUser ecmpUser = userService.selectEcmpUserById(userId);
		if (ecmpUser == null) {
			return ApiResponse.error("无此员工");
		}

		//当前是司机(新任务/派车/改派通知)
		if ("1".equals(ecmpUser.getItIsDriver())) {
			List<DriverInfo> driverInfos = driverInfoService.selectDriverInfoList(new DriverInfo(userId));
			if(CollectionUtils.isEmpty(driverInfos)){
				return ApiResponse.error("此用户不是司机");
			}
			DriverInfo driverInfo=driverInfos.get(0);
			String states= OrderState.ALREADYSENDING.getState();//订单状态
			//新任务通知
			MessageDto newOrderMessage = orderInfoService.getOrderMessage(null,states,driverInfo.getDriverId());
			if (newOrderMessage!=null)
				list.add(newOrderMessage);
			//改派通知
			MessageDto traceMessage = orderStateTraceInfoService.getTraceMessage(null,true,driverInfo.getDriverId());
			if (traceMessage!=null)
				list.add(traceMessage);
			//任务取消 TODO 逻辑可能有出入
			MessageDto cancelMessage = orderInfoService.getCancelOrderMessage(loginUser.getUser().getUserId(),OrderState.ORDERCANCEL.getState());
			if (cancelMessage!=null)
				list.add(cancelMessage);
		}
		//当前登录人为车队管理员(派车通知)
		if (ecmpUser!=null&& "1".equals(ecmpUser.getItIsDispatcher())){
			String states=OrderState.WAITINGLIST.getState();//订单状态
			MessageDto orderMessage = orderInfoService.getOrderMessage(loginUser.getUser().getUserId(),states,null);
			list.add(orderMessage);
		}
		return ApiResponse.success(list);
	}

    /**
     * 根据身份获取消息列表
     */
    @ApiOperation(value = "getMessagesList", notes = "根据身份获取消息列表", httpMethod ="POST")
    @PostMapping("/getMessagesList")
    public ApiResponse<List<EcmpMessage>> getMessagesList(String identity) {
        //获取登录用户
        List<EcmpMessage> list = ecmpMessageService.selectMessageList(identity);

        return ApiResponse.success(list);
    }

    /**
     * 更改消息状态为以读
     */
    @ApiOperation(value = "updateMessagesStatus", notes = "获取消息列表", httpMethod ="POST")
    @PostMapping("/updateMessagesStatus")
    public ApiResponse<List<EcmpMessage>> updateMessagesStatus(Long msgId) {
        EcmpMessage ecmpMessage = EcmpMessage.builder().id(msgId).status(MsgStatusConstant.MESSAGE_STATUS_T001.getType()).build();
        ecmpMessageService.update(ecmpMessage);
        return ApiResponse.success();
    }

    /**
     * 获取消息详情
     */
    @ApiOperation(value = "getMessages", notes = "获取消息详情", httpMethod ="POST")
    @PostMapping("/getMessages")
    public ApiResponse getMessagesList(Long msgId) {
        EcmpMessage msg = ecmpMessageService.queryById(msgId);
        return ApiResponse.success(msg);
    }

    /**
     * 根据未读消息条数
     */
    @ApiOperation(value = "getMessagesCount", notes = "根据未读消息条数", httpMethod ="POST")
    @PostMapping("/getMessagesCount")
    public ApiResponse getMessagesCount(String identity) {
        //获取登录用户
        int count = ecmpMessageService.getMessagesCount(identity);
        return ApiResponse.success(count);
    }
}
