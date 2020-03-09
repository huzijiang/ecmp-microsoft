package com.hq.ecmp.ms.api.controller.journey;

import com.github.pagehelper.Page;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IApplyApproveResultInfoService resultInfoService;
    @Autowired
    private IRegimeInfoService regimeInfoService;
    @Autowired
    private IApproveTemplateNodeInfoService nodeInfoService;


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
    public ApiResponse  applyTravelCommit(@RequestBody ApplyTravelRequest travelCommitApply){
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
        if (ObjectUtils.isNotEmpty(journeyApplyDto)){
            JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
            return ApiResponse.success(journeyInfo);
        }
        return ApiResponse.error("获取行程申请对应的审批流信息异常");
    }

    /**
     * 获取用车制度 对应的审批流信息
     * @param regimeDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveNodesInfoByRegimeInfo",notes = "获取用车制度 对应的审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApplyApproveNodesInfoByRegimeInfo")
    public ApiResponse   getApplyApproveNodesInfoByApproveTemplate(RegimeDto regimeDto){
        if (ObjectUtils.isNotEmpty(regimeDto)){
            ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(regimeDto.getRegimenId());
            return ApiResponse.success(applyInfo);
        }
        return ApiResponse.error("获取用车制度对应的审批流信息异常");
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
     * @param
     * @return
     */
    @ApiOperation(value = "getPassengerOwnerApplies",notes = "获取乘客自身 行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerApplies")
    public ApiResponse<List<ApplyInfoDTO>>   getPassengerOwnerApplies(@RequestBody PageRequest applyPage){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        //分页查询乘客申请列表
        List<ApplyInfoDTO> applyInfoList = applyInfoService.selectApplyInfoListByPage(loginUser.getUser()
                .getUserId(),applyPage.getPageNum(),applyPage.getPageSize());
        if(CollectionUtils.isEmpty(applyInfoList)){
            return ApiResponse.error("未查到申请列表数据");
        }
        return ApiResponse.success(applyInfoList);
    }

    /**
     * 获取乘客自身 行程申请详细信息
     * @param journeyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "getPassengerOwnerAppliesDetailInfo",notes = "获取乘客自身的行程申请详细信息 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerAppliesDetailInfo")
    public ApiResponse<ApplyDetailVO>   getPassengerOwnerAppliesDetailInfo(@RequestBody JourneyApplyDto journeyApplyDto){
        //根据applyId查询申请详情
        ApplyDetailVO  applyDetailVO = applyInfoService.selectApplyDetail(journeyApplyDto.getApplyId());
        if (applyDetailVO == null){
            return ApiResponse.error("查询申请详情异常");
        }
        return ApiResponse.success(applyDetailVO);
    }

    /**
     * 行程申请-审核通过
     * @param journeyApplyDto  审批员信息
     * @return
     */
    @ApiOperation(value = "applyPass",notes = "行程申请-审核通过 ",httpMethod ="POST")
    @PostMapping("/applyPass")
    @Transactional
    public ApiResponse applyPass(JourneyApplyDto journeyApplyDto){
        //1.校验信息
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try{
            List<ApplyApproveResultInfo> collect = beforeInspect(journeyApplyDto, userId);
            ApproveTemplateNodeInfo approveTemplateNodeInfo = nodeInfoService.selectApproveTemplateNodeInfoById(collect.get(0).getApproveNodeId());
            //判断当前审批人是不是最后审批人
            if (approveTemplateNodeInfo != null && approveTemplateNodeInfo.getNextNodeId() != null) {//不是最后审批人
                //修改当前审批记录状态已审批/审批通过，修改下一审批记录为待审批（）对应消息通知
                this.updateApproveResult(collect, userId);
                //下一审批人修改为待审批
                resultInfoService.updateApplyApproveResultInfo(new ApplyApproveResultInfo(collect.get(0).getApplyId(), collect.get(0).getApproveTemplateId(), collect.get(0).getApproveNodeId(), ApproveStateEnum.WAIT_APPROVE_STATE.getKey()));
                //TODO 发送通知消息
            } else if (approveTemplateNodeInfo != null && approveTemplateNodeInfo.getNextNodeId() == null) {//是最后节点审批人
                //修改审理状态
                this.updateApproveResult(collect, userId);
                //TODO 调取生成用车权限,初始化订单
                orderInfoService.initOrder(journeyApplyDto.getApplyId(),journeyApplyDto.getJouneyId(),userId);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
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
    public ApiResponse  applyReject(JourneyApplyDto journeyApplyDto,UserDto userDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try{
            List<ApplyApproveResultInfo> collect = beforeInspect(journeyApplyDto, userId);
            this.updateApproveResult(collect, userId);
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("行程申请单审批驳回");

    }


    /**
     * 获取行程申请的审批详情
     * @param journeyApplyDto  行程申请信息
     * @return
     */
    @ApiOperation(value = "getApplyApproveDetailInfo",notes = "获取行程申请的审批详情",httpMethod ="POST")
    @PostMapping("/getApplyApproveDetailInfo")
    public ApiResponse<TravelApprovalVO> getApplyApproveDetailInfo(JourneyApplyDto journeyApplyDto){
        TravelApprovalVO vo=new TravelApprovalVO();
        ApplyDetailVO  applyDetailVO = applyInfoService.selectApplyDetail(journeyApplyDto.getApplyId());
        //查询审批流信息
        List<ApprovalInfoVO> approveList = getApproveList(applyDetailVO.getApplyUser(), applyDetailVO.getApplyMobile(), journeyApplyDto.getApplyId());
        vo.setApplyDetailVO(applyDetailVO);
        vo.setApprovalVOS(approveList);
        return ApiResponse.success(vo);
    }

    @ApiOperation(value = "getApplyApproveCount",notes = "获取行程申请的审批详情",httpMethod ="POST")
    @PostMapping("/getApplyApproveCount")
    public ApiResponse<String> getApplyApproveCount(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
       int count= applyInfoService.getApplyApproveCount(userId);
        return ApiResponse.success(count+"");
    }


    //审批流信息排序
    private List<ApprovalInfoVO> getApproveList(String applyUser,String applyMobile,Long applyId){
        List<ApplyApproveResultInfo> applyApproveResultInfos = resultInfoService.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(applyId));
        List<ApprovalInfoVO> list=new ArrayList<>();
        list.add(new ApprovalInfoVO(0l,applyUser,applyMobile,"发起申请","申请人"));
        if (CollectionUtils.isNotEmpty(applyApproveResultInfos)){
            for (ApplyApproveResultInfo info:applyApproveResultInfos){
                list.add(new ApprovalInfoVO(info.getApproveNodeId(),info.getApprover(),info.getApproverMobile(), ApproveStateEnum.format(info.getApproveResult()),ApproveStateEnum.format(info.getState())));
            }
        }
        Collections.sort(list, new Comparator<ApprovalInfoVO>() {
            @Override
            public int compare(ApprovalInfoVO o1, ApprovalInfoVO o2) {
                int i = o1.getApprovalNodeId().intValue() - o2.getApprovalNodeId().intValue();
                if(i == 0){
                    return o1.getApprovalNodeId().intValue() - o2.getApprovalNodeId().intValue();
                }
                return i;
            }
        });
        return list;
    }

    private void updateApproveResult(List<ApplyApproveResultInfo> collect, Long userId) {
        if (CollectionUtils.isNotEmpty(collect) && collect.size() > 1) {
            for (ApplyApproveResultInfo info : collect) {//下一审批人为多个
                info.setApproveResult(ApproveStateEnum.APPROVE_PASS.getKey());
                info.setState(ApproveStateEnum.COMPLETE_APPROVE_STATE.getKey());
                info.setUpdateBy(String.valueOf(userId));
                info.setUpdateTime(new Date());
                resultInfoService.updateApplyApproveResultInfo(info);//审批通过
            }
        }
    }


    private List<ApplyApproveResultInfo> beforeInspect(JourneyApplyDto journeyApplyDto,Long userId) throws Exception{
        JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyApplyDto.getJouneyId());
        if (ObjectUtils.isEmpty(journeyInfo)){
            throw new Exception("行程申请单不存在!");
        }
        ApplyInfo applyInfo1 = applyInfoService.selectApplyInfoById(journeyApplyDto.getApplyId());
        if (ObjectUtils.isEmpty(applyInfo1)){
            throw new Exception("行程申请单不存在!");
        }
        RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(journeyInfo.getRegimenId());
        List<ApproveTemplateNodeInfo> approveTemplateNodeInfos = nodeInfoService.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(regimeInfo.getApproveTemplateId(), userId));
        List<ApplyApproveResultInfo> applyApproveResultInfos = resultInfoService.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(journeyApplyDto.getApplyId(), regimeInfo.getApproveTemplateId(), approveTemplateNodeInfos.get(0).getApproveNodeId()));
        if (CollectionUtils.isNotEmpty(applyApproveResultInfos)&&ApproveStateEnum.COMPLETE_APPROVE_STATE.getKey().equals(applyApproveResultInfos.get(0).getState())){
            throw new Exception("已审批");
        }
        List<ApplyApproveResultInfo> resultInfoList = resultInfoService.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(journeyApplyDto.getApplyId(),regimeInfo.getApproveTemplateId()));
        //所有待审批的记录
        List<ApplyApproveResultInfo> collect = resultInfoList.stream().filter(p -> ApproveStateEnum.WAIT_APPROVE_STATE.getKey().equals(p.getState()) && StringUtils.isBlank(p.getApproveResult())).collect(Collectors.toList());
        List<Long> nodeIds = collect.stream().map(ApplyApproveResultInfo::getApproveNodeId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(nodeIds)){
            String nodeInfos= nodeInfoService.getListByNodeIds(nodeIds);
            if (!nodeInfos.contains(String.valueOf(userId))){
                throw new Exception("您不是当前审批人!");
            }
        }
        return collect;
    }
}
