package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
		MessageDto traceMessage=orderStateTraceInfoService.getTraceMessage(loginUser.getUser().getUserId());
		//如果当前登录人是审批人(审批通知) TODO
		MessageDto approveMassage=approveResultInfoService.getApproveMessage(loginUser.getUser().getUserId());
		//当前登录人为车队管理员(派车通知)
		EcmpUser ecmpUser = userService.selectEcmpUserById(loginUser.getUser().getUserId());
		if (ecmpUser!=null&& "1".equals(ecmpUser.getItIsDispatcher())){
			String states="S100,S101,S000";//订单状态
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
		List<MessageDto> list = new ArrayList<>();
		EcmpUser ecmpUser = userService.selectEcmpUserById(loginUser.getUser().getUserId());
		if (ecmpUser == null) {
			return ApiResponse.error("无此员工");
		}

		//当前是司机(新任务/派车/改派通知)
		if ("1".equals(ecmpUser.getItIsDriver())) {
			String states="S299";//订单状态
			//新任务通知
			MessageDto newOrderMessage = orderInfoService.getOrderMessage(null,states,loginUser.getUser().getUserId());
			//改派通知
			MessageDto traceMessage = orderStateTraceInfoService.getTraceMessage(loginUser.getUser().getUserId());
			//任务取消
		}
		//当前登录人为车队管理员(派车通知)
		if (ecmpUser!=null&& "1".equals(ecmpUser.getItIsDispatcher())){
			String states="S100,S101,S000";//订单状态
			MessageDto orderMessage = orderInfoService.getOrderMessage(loginUser.getUser().getUserId(),states,null);
			list.add(orderMessage);
		}
		return ApiResponse.success(list);
	}
}
