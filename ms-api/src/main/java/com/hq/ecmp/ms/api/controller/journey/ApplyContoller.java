package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.ApplyStateConstant;
import com.hq.ecmp.constant.ApplyTypeEnum;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.constant.CarLeaveEnum;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.ms.api.dto.base.RegimeDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.ms.api.dto.journey.JourneyApplyDto;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hq.ecmp.constant.CommonConstant.*;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 13:18
 */
@RestController
@RequestMapping("/apply")
@Slf4j
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
    private IEcmpUserService ecmpUserService;
    @Autowired
    private EcmpMessageService ecmpMessageService;
    @Autowired
    private IJourneyUserCarPowerService journeyUserCarPowerService;



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
    @Log("员工提交公务申请")
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:提交公务申请", businessType = BusinessType.INSERT)
    @ApiOperation(value = "applyOfficialCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyOfficialCommit")
    public ApiResponse<ApplyVO>   applyOfficialCommit(@RequestBody ApplyOfficialRequest officialCommitApply){
        //提交公务行程申请
        ApplyVO applyVO = null;
        try {
            applyVO = applyInfoService.applyOfficialCommit(officialCommitApply);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交公务申请失败");
        }
        return ApiResponse.success("提交申请成功",applyVO);
    }

    /**
     * 员工提交差旅行程申请
     * @param  travelCommitApply  行程申请信息
     * @param
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:提交差旅申请", businessType = BusinessType.INSERT)
    @ApiOperation(value = "applyTravelCommit",notes = "员工提交行程申请，行程信息必须全面 ",httpMethod ="POST")
    @PostMapping("/applyTravelCommit")
    public ApiResponse<ApplyVO>  applyTravelCommit(@RequestBody ApplyTravelRequest travelCommitApply){
        //提交差旅行程申请
        ApplyVO applyVO = null;
        try {
            applyVO = applyInfoService.applytravliCommit(travelCommitApply);

        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("提交差旅申请失败");
        }
        return ApiResponse.success("提交申请成功",applyVO);
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
     * 根据申请单查询审批流信息
     * @param journeyApplyDto  申请信息
     * @return
     */
    @ApiOperation(value = "getApproveResult",notes = "根据申请单查询审批流信息 ",httpMethod ="POST")
    @PostMapping("/getApproveResult")
    public ApiResponse <List<ApprovalListVO>> getApproveResult(JourneyApplyDto journeyApplyDto){
        ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(journeyApplyDto.getApplyId());
        if (applyInfo==null){
            return ApiResponse.error("此行程申请不存在");
        }
        EcmpUser ecmpUser = ecmpUserService.selectEcmpUserById(Long.parseLong(applyInfo.getCreateBy()));
        List<ApprovalListVO> approveList = applyInfoService.getApproveList(ecmpUser.getNickName(), ecmpUser.getPhonenumber(), journeyApplyDto.getApplyId(),applyInfo.getCreateTime());
        return ApiResponse.success(approveList);
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
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:申请列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getPassengerOwnerApplies",notes = "获取乘客自身 行程申请列表 ",httpMethod ="POST")
    @PostMapping("/getPassengerOwnerApplies")
    public ApiResponse<PageResult<ApplyInfoDTO>>   getPassengerOwnerApplies(@RequestBody PageRequest applyPage){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        //分页查询乘客申请列表
        PageResult<ApplyInfoDTO> applyInfoList = applyInfoService.selectApplyInfoListByPage(loginUser.getUser()
                .getUserId(),applyPage.getPageNum(),applyPage.getPageSize());
        return ApiResponse.success(applyInfoList);
    }

    /**
     * 获取乘客自身 行程申请详细信息
     * @param journeyApplyDto  审批员信息
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:申请详情", businessType = BusinessType.OTHER)
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
    public ApiResponse applyPass(@RequestBody ApplyDTO journeyApplyDto){
        //1.校验信息
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try{
            ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(journeyApplyDto.getApplyId());
            List<ApplyApproveResultInfo> resultInfoList = beforeInspect(journeyApplyDto, userId);
            //判断当前审批人是不是最后审批人
            Map<String, List<ApplyApproveResultInfo>> resultMap = resultInfoList.stream().collect(Collectors.groupingBy(ApplyApproveResultInfo::getState));
            List<ApplyApproveResultInfo> waitcollect = resultMap.get(ApproveStateEnum.WAIT_APPROVE_STATE.getKey());
            String nextNodeId=waitcollect.get(0).getNextNodeId();
            log.info("申请单:"+journeyApplyDto.getApplyId()+"的下一审批人为"+nextNodeId);
            if (CollectionUtils.isNotEmpty(waitcollect)&&!"0".equals(nextNodeId)) {//不是最后审批人
                //修改当前审批记录状态已审批/审批通过，修改下一审批记录为待审批（）对应消息通知
                this.updateApproveResult(waitcollect, userId,ApproveStateEnum.APPROVE_PASS.getKey(),"该订单审批通过");
                log.info("申请单:"+journeyApplyDto.getApplyId()+"当前被"+userId+"审批通过");
                List<ApplyApproveResultInfo> noArrivecollect = resultMap.get(ApproveStateEnum.NOT_ARRIVED_STATE.getKey());
                //修改下一审批节点为待审批
                this.updateNextApproveResult(noArrivecollect,Long.parseLong(nextNodeId),journeyApplyDto.getApplyId(),userId);
                log.info("申请单:"+journeyApplyDto.getApplyId()+"修改下一审批节点"+nextNodeId+"待审批");
            } else if (CollectionUtils.isNotEmpty(waitcollect)&&"0".equals(waitcollect.get(0).getNextNodeId())) {//是最后节点审批人
                //修改审理状态
                this.updateApproveResult(waitcollect, userId,ApproveStateEnum.APPROVE_PASS.getKey(),"该订单审批通过");
                ApplyInfo info = ApplyInfo.builder().applyId(journeyApplyDto.getApplyId()).state(ApplyStateConstant.APPLY_PASS).build();
                applyInfoService.updateApplyInfo(info);
                //TODO 调取生成用车权限,初始化订单
                boolean optFlag = journeyUserCarPowerService.createUseCarAuthority(journeyApplyDto.getApplyId(), userId);
                if(!optFlag){
                    return ApiResponse.error("生成用车权限失败");
                }
                List<CarAuthorityInfo> carAuthorityInfos = journeyUserCarPowerService.queryOfficialOrderNeedPower(applyInfo.getJourneyId());
                if (CollectionUtils.isNotEmpty(carAuthorityInfos)){
                    int flag=carAuthorityInfos.get(0).getDispatchOrder()?ONE:ZERO;
                    ecmpMessageService.applyUserPassMessage(journeyApplyDto.getApplyId(),Long.parseLong(applyInfo.getCreateBy()),userId,null,carAuthorityInfos.get(0).getTicketId(),flag);
                    for (CarAuthorityInfo carAuthorityInfo:carAuthorityInfos){
                        int isDispatch=carAuthorityInfo.getDispatchOrder()?ONE:TWO;
                        OfficialOrderReVo officialOrderReVo = new OfficialOrderReVo(carAuthorityInfo.getTicketId(),isDispatch, CarLeaveEnum.getAll());
                        Long orderId=null;
                        if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyInfo.getApplyType())){
                            orderId = orderInfoService.officialOrder(officialOrderReVo, userId);
                        }
                        ecmpMessageService.saveApplyMessagePass(journeyApplyDto.getApplyId(),Long.parseLong(applyInfo.getCreateBy()),userId,orderId,carAuthorityInfos.get(0).getTicketId(),isDispatch);
                    }
                }
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
     * @return
     */
    @ApiOperation(value = "applyReject",notes = "行程申请-驳回",httpMethod ="POST")
    @PostMapping("/applyReject")
    public ApiResponse  applyReject(@RequestBody ApplyDTO journeyApplyDto){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try{
            List<ApplyApproveResultInfo> allcollect = beforeInspect(journeyApplyDto, userId);
            List<ApplyApproveResultInfo> collect = allcollect.stream().filter(p -> ApproveStateEnum.WAIT_APPROVE_STATE.getKey().equals(p.getState()) && StringUtils.isBlank(p.getApproveResult())).collect(Collectors.toList());
            this.updateApproveResult(collect, userId,ApproveStateEnum.APPROVE_FAIL.getKey(),journeyApplyDto.getRejectReason());
            ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(journeyApplyDto.getApplyId());
            applyInfo.setState(ApplyStateConstant.REJECT_APPLY);
            applyInfo.setUpdateBy(String.valueOf(userId));
            applyInfo.setUpdateTime(new Date());
            applyInfoService.updateApplyInfo(applyInfo);
            log.info("申请单:"+journeyApplyDto.getApplyId()+"当前被"+userId+"审批驳回");
            ecmpMessageService.saveApplyMessageReject(journeyApplyDto.getApplyId(),Long.parseLong(applyInfo.getCreateBy()),userId,journeyApplyDto.getRejectReason());
        }catch (Exception e){
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success("行程申请单审批驳回");

    }

    /**
     * 审批列表
     * @param approveInfoDTO
     * @return
     */
    @ApiOperation(value = "getApprovePage",notes = "审批列表 ",httpMethod ="POST")
    @PostMapping("/getApprovePage")
    public ApiResponse<PageResult<ApprovaReesultVO>> getApprovePage(@RequestBody ApproveInfoDTO approveInfoDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId=loginUser.getUser().getUserId();
        List<ApprovaReesultVO> page= applyInfoService.getApprovePage(approveInfoDTO.getPageIndex(),approveInfoDTO.getPageSize(),userId);
        Integer count=applyInfoService.getApprovePageCount(userId);
        return ApiResponse.success(new PageResult<ApprovaReesultVO>(Long.valueOf(count), page));
    }

    /**
     * 获取审批详情
     * @param journeyApplyDto  行程申请信息
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "申请模块:申请/审批详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getTravelApproval",notes = "获取审批详情",httpMethod ="POST")
    @PostMapping("/getTravelApproval")
    public ApiResponse<TravelApprovalVO> getApplyApproveDetailInfo(@RequestBody ApplyDTO journeyApplyDto){
        TravelApprovalVO vo=new TravelApprovalVO();
        ApplyDetailVO  applyDetailVO = applyInfoService.selectApplyDetail(journeyApplyDto.getApplyId());
        //查询审批流信息
        //如果申请单无需审批则不展示审批流相关信息
        List<ApprovalListVO> approveList =applyInfoService.getApproveList(applyDetailVO.getApplyUser(), applyDetailVO.getApplyMobile(), journeyApplyDto.getApplyId(),applyDetailVO.getTime());
        vo.setApprovalVOS(approveList);
        vo.setApplyDetailVO(applyDetailVO);
        return ApiResponse.success(vo);
    }

    /**
     * 获取我的申请与审批个数
     * @return
     */
    @ApiOperation(value = "getApplyApproveCount",notes = "获取我的申请与审批个数",httpMethod ="POST")
    @PostMapping("/getApplyApproveCount")
    public ApiResponse<String> getApplyApproveCount(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
       int count= applyInfoService.getApplyApproveCount(userId);
        return ApiResponse.success("查询成功",count+"");
    }



    private void updateApproveResult(List<ApplyApproveResultInfo> collect, Long userId,String result,String content) {
        if (CollectionUtils.isNotEmpty(collect)) {
            for (ApplyApproveResultInfo info : collect) {//下一审批人为多个
                info.setApproveResult(result);
                info.setContent(content);
                info.setState(ApproveStateEnum.COMPLETE_APPROVE_STATE.getKey());
                info.setUpdateBy(String.valueOf(userId));
                info.setUpdateTime(new Date());
                resultInfoService.updateApplyApproveResultInfo(info);//审批通过
            }
        }
    }


    private List<ApplyApproveResultInfo> beforeInspect(ApplyDTO journeyApplyDto,Long userId) throws Exception{
        ApplyInfo applyInfo1 = applyInfoService.selectApplyInfoById(journeyApplyDto.getApplyId());
        if (ObjectUtils.isEmpty(applyInfo1)){
            throw new Exception("行程申请单不存在!");
        }
        if (!ApplyStateConstant.ON_APPLYING.equals(applyInfo1.getState())){
            throw new Exception("该申请单已审批或已撤销");
        }
        JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(applyInfo1.getJourneyId());
        if (ObjectUtils.isEmpty(journeyInfo)){
            throw new Exception("行程申请单不存在!");
        }
        if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyInfo1.getApplyType())){
            if (journeyInfo.getUseCarTime().getTime()<new Date().getTime()){//申请单已过期
                applyInfoService.updateApplyState(journeyApplyDto.getApplyId(),ApplyStateConstant.EXPIRED_APPLY,ApproveStateEnum.EXPIRED_APPROVE_STATE.getKey(),userId);
                throw new Exception("申请单:"+applyInfo1.getApplyId()+"已过期");
            }
        }else{//差旅
            int i = DateFormatUtils.compareDay(journeyInfo.getStartDate(), new Date());
            if (i==ONE){//申请单已过期
                applyInfoService.updateApplyState(journeyApplyDto.getApplyId(),ApplyStateConstant.EXPIRED_APPLY,ApproveStateEnum.EXPIRED_APPROVE_STATE.getKey(),userId);
                throw new Exception("申请单:"+applyInfo1.getApplyId()+"已过期");
            }
        }
        RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(applyInfo1.getRegimenId());
        List<ApplyApproveResultInfo> applyApproveResultInfos = resultInfoService.selectByUserId(journeyApplyDto.getApplyId(), userId,null);
        if (CollectionUtils.isEmpty(applyApproveResultInfos)){
            throw new Exception("您未有此申请单的审批权限");
        }
        List<ApplyApproveResultInfo> resultInfoList = resultInfoService.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(journeyApplyDto.getApplyId(),regimeInfo.getApproveTemplateId()));
        //所有待审批的记录
        List<ApplyApproveResultInfo> collect = resultInfoList.stream().filter(p -> ApproveStateEnum.WAIT_APPROVE_STATE.getKey().equals(p.getState()) && StringUtils.isBlank(p.getApproveResult())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)){
            for (ApplyApproveResultInfo info:collect){
                if (StringUtils.isBlank(info.getApproveUserId())||!info.getApproveUserId().contains(String.valueOf(userId))){
                    throw new Exception("此申请单还未到达您审批!");
                }
            }
        }else{
            String state=ApproveStateEnum.format(resultInfoList.get(0).getState());
            throw new Exception("该申请单:"+resultInfoList.get(0).getApplyId()+ state);
        }
        return resultInfoList;
    }

    private void updateNextApproveResult(List<ApplyApproveResultInfo> noArrivecollect,Long nextNodeId,Long applyId,Long userId){
        //下一审批人修改为待审批
        if (CollectionUtils.isNotEmpty(noArrivecollect)) {
            for (ApplyApproveResultInfo resultInfo:noArrivecollect){
                if(ApproveStateEnum.NOT_ARRIVED_STATE.getKey().equals(resultInfo.getState())&&nextNodeId==resultInfo.getApproveNodeId()){
                    resultInfo.setState(ApproveStateEnum.WAIT_APPROVE_STATE.getKey());
                    resultInfo.setUpdateTime(new Date());
                    resultInfo.setUpdateBy(userId+"");
                    resultInfoService.updateApplyApproveResultInfo(resultInfo);
                    //给下一审批人发送消息
                    //TODO 第一期发起申请就会给所有级审批员发消息
                    ecmpMessageService.sendNextApproveUsers(resultInfo.getApproveUserId(),applyId,userId);
                }
            }
        }
    }

}
