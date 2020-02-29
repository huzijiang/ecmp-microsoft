package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.dto.ApplyTravelRequest;
import com.hq.ecmp.mscore.dto.JourneyCommitApplyDto;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.vo.AddressVO;
import com.hq.ecmp.mscore.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hq.ecmp.mscore.dto.ApplyOfficialRequest;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:18
 */
@RestController
@RequestMapping("/apply")
public class ApplyContoller {

    @Autowired
    private IApplyInfoService applyInfoService;

    @Autowired
    private IJourneyInfoService journeyInfoService;

    @Autowired
    private IOrderInfoService orderInfoService;

    /**
     * 员工提交行程申请     弃用。公务跟差旅申请分两个接口实现
     * @param  journeyCommitApplyDto  行程申请信息
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "applyCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyCommit")
    public ApiResponse   applyCommit(JourneyCommitApplyDto journeyCommitApplyDto){
        //提交行程申请
        applyInfoService.applyCommit(journeyCommitApplyDto);
        return ApiResponse.success();
    }

    /**
     * 员工提交公务行程申请
     * @param  officialCommitApply  行程申请信息
     * @param
     * @return
     */
    @ApiOperation(value = "applyOfficialCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyOfficialCommit")
    public ApiResponse   applyOfficialCommit(@RequestBody ApplyOfficialRequest officialCommitApply){
        //提交公务行程申请
        try {
            applyInfoService.applyOfficialCommit(officialCommitApply);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交公务申请失败");
        }
        return ApiResponse.success("提交申请成功");
    }

    /**
     * 员工提交差旅行程申请
     * @param  travelCommitApply  行程申请信息
     * @param
     * @return
     */
    @ApiOperation(value = "applyTravelCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyTravelCommit")
    public ApiResponse   applyTravelCommit(@RequestBody ApplyTravelRequest travelCommitApply){
        //提交差旅行程申请
        try {
            applyInfoService.applytravliCommit(travelCommitApply);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交差旅申请失败");
        }
        return ApiResponse.success("提交申请成功");
    }

    /**
     * 获取行程申请 对应的审批流信息
     * @param journeyApplyDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveNodesInfo",notes = "获取行程申请 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproveNodesInfo")
    public ApiResponse   getApplyApproveNodesInfo(JourneyApplyDto journeyApplyDto){
        return null;
    }

    /**
     * 获取用车制度 对应的审批流信息
     * @param regimeDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveNodesInfoByRegimeInfo",notes = "获取用车制度 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproveNodesInfoByRegimeInfo")
    public ApiResponse   getApplyApproveNodesInfoByApproveTemplate(RegimeDto regimeDto){

        return null;
    }


    /**
     * 获取等待审批员 审批的 行程申请列表
     * @param userDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getWaitApproveApplies",notes = "获取等待当前用户审批的行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getWaitApproveApplies")
    public ApiResponse   getWaitApproveApplies(UserDto userDto){
        JourneyInfo journeyInfo = JourneyInfo.builder().userId(userDto.getUserId()).build();
        // PageHelper.startPage(userDto.get)
        List<JourneyInfo> journeyInfos = journeyInfoService.selectJourneyInfoList(journeyInfo);
        return ApiResponse.success("查询单前用户可审批行程单列表成功",journeyInfos);
    }

    /**
     * 获取等待审批员 审批的 行程申请详细信息
     * @param jouneyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getWaitApproveApplyDetailInfo",notes = "获取待审批的行程申请详细信息 ",httpMethod ="POST")
    @PostMapping("/getWaitApproveApplyDetailInfo")
    public ApiResponse   getWaitApproveApplyDetailInfo(JourneyApplyDto jouneyApplyDto){

        return null;
    }


    /**
     * 获取乘客自身 行程申请列表
     * @param userDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getPassengerOwnerApplies",notes = "获取乘客自身 行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerApplies")
    public ApiResponse   getPassengerOwnerApplies(UserDto userDto){

        return null;
    }

    /**
     * 获取乘客自身 行程申请详细信息
     * @param journeyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getPassengerOwnerAppliesDetailInfo",notes = "获取乘客自身的行程申请详细信息 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerAppliesDetailInfo")
    public ApiResponse   getPassengerOwnerAppliesDetailInfo(JourneyApplyDto journeyApplyDto){

        return null;
    }

    /**
     * 行程申请-审核通过
     * @param journeyApplyDto  审批员信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "applyPass",notes = "行程申请-审核通过 ",httpMethod ="POST")
    @PostMapping("/applyPass")
    @Transactional
    public ApiResponse applyPass(JourneyApplyDto journeyApplyDto,UserDto userDto){
        //1.校验信息
        JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
        if (ObjectUtils.isEmpty(journeyInfo)){
            return ApiResponse.error("行程申请单不存在!");
        }
        ApplyInfo applyInfo = ApplyInfo.builder().journeyId(journeyApplyDto.getJouneyId()).build();
        List<ApplyInfo> applyInfos = applyInfoService.selectApplyInfoList(applyInfo);
        if (CollectionUtils.isNotEmpty(applyInfos)){
            return ApiResponse.error("行程申请单已审批！");
        }
        //2.生成行程单审批信息
        applyInfo = ApplyInfo.builder().applyId(Long.MAX_VALUE).applyType("").approverName("").journeyId(journeyInfo.getJourneyId()).costCenter(userDto.getUserId()).projectId(userDto.getUserId()).
                approverName("").reason("").state("").regimenId(Long.MAX_VALUE).build();
        applyInfo.setCreateBy(userDto.getUserName());
        applyInfo.setCreateTime(DateUtils.getNowDate());
        applyInfoService.insertApplyInfo(applyInfo);
        //3.默认生成订单
        OrderInfo orderInfo = OrderInfo.builder().orderId(Long.MAX_VALUE).journeyId(journeyInfo.getJourneyId()).build();
        orderInfoService.insertOrderInfo(orderInfo);
        return ApiResponse.success("行程申请单审批通过");
    }


    /**
     * 行程申请-驳回
     * @param journeyApplyDto  行程申请信息
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "applyReject",notes = "行程申请-驳回",httpMethod ="POST")
    @PostMapping("/applyReject")
    public ApiResponse   applyReject(JourneyApplyDto journeyApplyDto,UserDto userDto){

        //1.校验消息
        JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
        if (ObjectUtils.isEmpty(journeyInfo)){
            return ApiResponse.error("行程申请单不存在！");
        }
        ApplyInfo applyInfo = ApplyInfo.builder().journeyId(journeyApplyDto.getJouneyId()).build();
        List<ApplyInfo> applyInfos = applyInfoService.selectApplyInfoList(applyInfo);
        if (CollectionUtils.isNotEmpty(applyInfos)){
            return ApiResponse.error("行程申请单已审批");
        }
        //2.生成行程单审批信息
        //state审批状态(1表示审批通过，0表示审批驳回)
        applyInfo = ApplyInfo.builder().applyId(Long.MAX_VALUE).applyType("").approverName("").journeyId(journeyInfo.getJourneyId()).costCenter(userDto.getUserId()).projectId(userDto.getUserId())
                .approverName("").reason("").state("").regimenId(Long.MAX_VALUE).build();
        applyInfo.setCreateBy(userDto.getUserName());
        applyInfo.setCreateTime(DateUtils.getNowDate());
        applyInfoService.insertApplyInfo(applyInfo);
        //3.默认生成订单
        OrderInfo orderInfo = OrderInfo.builder().orderId(Long.MAX_VALUE).journeyId(journeyInfo.getJourneyId()).build();
        orderInfoService.insertOrderInfo(orderInfo);
        return ApiResponse.success("行程申请单审批驳回");

    }


    /**
     * 获取行程申请的审批详情
     * @param journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveDetailInfo",notes = "获取行程申请的审批详情",httpMethod ="POST")
    @PostMapping("/getApplyApproveDetailInfo")
    public ApiResponse   getApplyApproveDetailInfo(JourneyApplyDto journeyApplyDto){
        JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
        return ApiResponse.success("查询申请单详情成功",journeyInfo);
    }


}
