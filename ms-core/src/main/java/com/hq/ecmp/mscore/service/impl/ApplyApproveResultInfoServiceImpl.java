package com.hq.ecmp.mscore.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ApplyDTO;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.ApprovalInfoVO;
import com.hq.ecmp.mscore.vo.OfficialOrderReVo;
import com.hq.ecmp.mscore.vo.UserVO;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.SortListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.hq.ecmp.constant.CommonConstant.*;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class ApplyApproveResultInfoServiceImpl implements IApplyApproveResultInfoService
{
    @Autowired
    private ApplyApproveResultInfoMapper applyApproveResultInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Autowired
    private ApproveTemplateInfoMapper approveTemplateInfoMapper;
    @Autowired
    private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private ApplyInfoMapper applyInfoMapper;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private EcmpUserRoleMapper userRoleMapper;
    @Resource
    private EcmpMessageService ecmpMessageService;
    @Resource
    @Lazy
    private IApplyInfoService applyInfoService;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private IOrderInfoService orderInfoService;
    @Resource
    private IJourneyUserCarPowerService journeyUserCarPowerService;

    /**
     * 查询【请填写功能名称】
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApplyApproveResultInfo selectApplyApproveResultInfoById(Long approveResultId)
    {
        return applyApproveResultInfoMapper.selectApplyApproveResultInfoById(approveResultId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApplyApproveResultInfo> selectApplyApproveResultInfoList(ApplyApproveResultInfo applyApproveResultInfo)
    {
        return applyApproveResultInfoMapper.selectApplyApproveResultInfoList(applyApproveResultInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo)
    {
        applyApproveResultInfo.setCreateTime(DateUtils.getNowDate());
        return applyApproveResultInfoMapper.insertApplyApproveResultInfo(applyApproveResultInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo)
    {
        applyApproveResultInfo.setUpdateTime(DateUtils.getNowDate());
        return applyApproveResultInfoMapper.updateApplyApproveResultInfo(applyApproveResultInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveResultIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyApproveResultInfoByIds(Long[] approveResultIds)
    {
        return applyApproveResultInfoMapper.deleteApplyApproveResultInfoByIds(approveResultIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyApproveResultInfoById(Long approveResultId)
    {
        return applyApproveResultInfoMapper.deleteApplyApproveResultInfoById(approveResultId);
    }

    @Override
    public MessageDto getApproveMessage(Long userId) {
        return applyApproveResultInfoMapper.getApproveMessage(userId);
    }

    @Override
    public List<ApprovalInfoVO> getApproveResultList(ApplyApproveResultInfo applyApproveResultInfo) {
        return applyApproveResultInfoMapper.getApproveResultList(applyApproveResultInfo.getApplyId(),applyApproveResultInfo.getApproveTemplateId());
    }

    @Override
    public List<ApplyApproveResultInfo> selectApproveResultByNodeids(String nextNodeId,String state) {
        return applyApproveResultInfoMapper.selectApproveResultByNodeids(nextNodeId,state);
    }

    @Override
    public List<ApplyApproveResultInfo> selectByUserId(Long applyId, Long userId,String state) {
        return applyApproveResultInfoMapper.selectByUserId(applyId,userId,state);
    }

    /**
     * 审批驳回
     * @param journeyApplyDto
     * @param userId
     * @param allcollect
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void applyReject(ApplyDTO journeyApplyDto, Long userId,List<ApplyApproveResultInfo> allcollect) throws Exception{
        List<ApplyApproveResultInfo> collect = allcollect.stream().filter(p -> ApproveStateEnum.WAIT_APPROVE_STATE.getKey().equals(p.getState()) && org.apache.commons.lang3.StringUtils.isBlank(p.getApproveResult())).collect(Collectors.toList());
        this.updateApproveResult(collect, userId,ApproveStateEnum.APPROVE_FAIL.getKey(),journeyApplyDto.getRejectReason());
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(journeyApplyDto.getApplyId());
        applyInfo.setState(ApplyStateConstant.REJECT_APPLY);
        applyInfo.setUpdateBy(String.valueOf(userId));
        applyInfo.setUpdateTime(new Date());
        applyInfoMapper.updateApplyInfo(applyInfo);
        log.info("申请单:"+journeyApplyDto.getApplyId()+"当前被"+userId+"审批驳回");
        ecmpMessageService.saveApplyMessageReject(journeyApplyDto.getApplyId(),Long.parseLong(applyInfo.getCreateBy()),userId,journeyApplyDto.getRejectReason());
    }

    /**
     * 审批通过
     * @param journeyApplyDto
     * @param loginUserId
     * @param resultInfoList
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void applyPass(ApplyDTO journeyApplyDto, Long loginUserId,List<ApplyApproveResultInfo> resultInfoList) throws Exception {
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(journeyApplyDto.getApplyId());
//        List<ApplyApproveResultInfo> resultInfoList = this.beforeInspect(journeyApplyDto, userId);
        //判断当前审批人是不是最后审批人
        Map<String, List<ApplyApproveResultInfo>> resultMap = resultInfoList.stream().collect(Collectors.groupingBy(ApplyApproveResultInfo::getState));
        List<ApplyApproveResultInfo> waitcollect = resultMap.get(ApproveStateEnum.WAIT_APPROVE_STATE.getKey());
        String nextNodeId=waitcollect.get(0).getNextNodeId();
        log.info("申请单:"+journeyApplyDto.getApplyId()+"的当前审批人为"+waitcollect.get(0).getApproveUserId());
        log.info("申请单:"+journeyApplyDto.getApplyId()+"的下一审批节点为"+nextNodeId);
        //不是最后审批人
        if (CollectionUtils.isNotEmpty(waitcollect)&&!String.valueOf(ZERO).equals(nextNodeId)) {
            //修改当前审批记录状态已审批/审批通过，修改下一审批记录为待审批（）对应消息通知
            this.updateApproveResult(waitcollect, loginUserId,ApproveStateEnum.APPROVE_PASS.getKey(),"该订单审批通过");
            log.info("申请单:"+journeyApplyDto.getApplyId()+"当前审批节点"+waitcollect.get(0).getApproveNodeId()+"被"+loginUserId+"审批通过");
            List<ApplyApproveResultInfo> noArrivecollect = resultMap.get(ApproveStateEnum.NOT_ARRIVED_STATE.getKey());
            //修改下一审批节点为待审批,给下一审判人发通知
            this.updateNextApproveResult(noArrivecollect,Long.parseLong(nextNodeId),journeyApplyDto.getApplyId(),loginUserId);
            log.info("申请单:"+journeyApplyDto.getApplyId()+"修改下一审批节点"+nextNodeId+"为待审批");
        } else if (CollectionUtils.isNotEmpty(waitcollect)&&"0".equals(waitcollect.get(0).getNextNodeId())) {
            //是最后节点审批人
            //修改审理状态
            this.updateApproveResult(waitcollect, loginUserId,ApproveStateEnum.APPROVE_PASS.getKey(),"该订单审批通过");
            ApplyInfo info = ApplyInfo.builder().applyId(journeyApplyDto.getApplyId()).state(ApplyStateConstant.APPLY_PASS).build();
            info.setUpdateTime(new Date());
            applyInfoMapper.updateApplyInfo(info);
            log.info("申请单:"+journeyApplyDto.getApplyId()+"被"+loginUserId+"审批通过");
            //TODO 调取生成用车权限,初始化订单
            boolean optFlag = journeyUserCarPowerService.createUseCarAuthority(journeyApplyDto.getApplyId(), loginUserId);
            if(!optFlag){
                throw new Exception("生成用车权限失败");
            }
            List<CarAuthorityInfo> carAuthorityInfos = journeyUserCarPowerService.queryOfficialOrderNeedPower(applyInfo.getJourneyId());
            if (CollectionUtils.isNotEmpty(carAuthorityInfos)){
                int flag=carAuthorityInfos.get(0).getDispatchOrder()?ONE:ZERO;
                /**给申请人发通知*/
                ecmpMessageService.applyUserPassMessage(journeyApplyDto.getApplyId(),Long.parseLong(applyInfo.getCreateBy()),loginUserId,null,carAuthorityInfos.get(0).getTicketId(),flag);
                for (CarAuthorityInfo carAuthorityInfo:carAuthorityInfos){
                    int isDispatch=carAuthorityInfo.getDispatchOrder()?ONE:TWO;
                    OfficialOrderReVo officialOrderReVo = new OfficialOrderReVo(carAuthorityInfo.getTicketId(),isDispatch, CarLeaveEnum.getAll());
                    Long orderId=null;
                    if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyInfo.getApplyType())){
                        orderId = orderInfoService.officialOrder(officialOrderReVo, loginUserId);
                    }
                    /**给调度员发通知/短信*/
                    ecmpMessageService.saveApplyMessagePass(applyInfo,loginUserId,orderId,carAuthorityInfos.get(0).getTicketId(),isDispatch);
                }
            }
        }
    }

    /**
     * 初始化审批流
     * @param applyId 申请id
     * @param regimenId 用车制度id
     * @param userId 登录人id
     */
    @Override
    public void initApproveResultInfo(Long applyId,Long regimenId,Long userId) throws Exception {
        //查询审批模板
        log.info("初始化审批流开始:applyId="+applyId+",regimenId="+regimenId+"userId"+userId);
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimenId);
        if (regimeVo!=null){
            if (CommonConstant.NO_PASS.equals(regimeVo.getNeedApprovalProcess())){
                log.info("申请单"+applyId+"无需审批");
                applyInfo.setState(ApplyStateConstant.APPLY_PASS);
                applyInfo.setUpdateTime(new Date());
                applyInfo.setUpdateBy(String.valueOf(userId));
                applyInfoMapper.updateApplyInfo(applyInfo);
                log.info("申请单"+applyId+"状态为"+applyInfo.getState());
                return;
            }
            List<ApproveTemplateNodeInfo> approveTemplateNodeInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(Long.valueOf(regimeVo.getApproveTemplateId())));
            SortListUtil.sort(approveTemplateNodeInfos,"approveNodeId",SortListUtil.ASC);
            if (CollectionUtils.isNotEmpty(approveTemplateNodeInfos)){
                for (int i=0;i<approveTemplateNodeInfos.size();i++ ){
                    ApproveTemplateNodeInfo info = approveTemplateNodeInfos.get(i);
                    ApplyApproveResultInfo resultInfo=new ApplyApproveResultInfo(applyId,Long.valueOf(regimeVo.getApproveTemplateId()),info.getApproveNodeId(),info.getApproverType(),info.getNextNodeId());
                    resultInfo.setCreateTime(new Date());
                    resultInfo.setCreateBy(String.valueOf(userId));
                    String state= ApproveStateEnum.NOT_ARRIVED_STATE.getKey();
                    if (i==0){
                        state=ApproveStateEnum.WAIT_APPROVE_STATE.getKey();
                    }
                    resultInfo.setState(state);
                    switch (ApproveTypeEnum.format(info.getApproverType())) {
                        case  APPROVE_T001://部门负责人
                            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(Long.parseLong(applyInfo.getCreateBy()));
                            String deptUser=ecmpUserMapper.findDeptLeader(ecmpUser.getDeptId());
                            if (deptUser==null){
                                deptUser= this.getOrgByDeptId(ecmpUser.getDeptId());
                            }
                            if (deptUser==null){
                                log.error("用车制度:"+regimenId+"对应审批模板项目主管审批未设置主管");
                                throw new Exception("该公司未设置领导层");
                            }
                            resultInfo.setApproveUserId(deptUser);
                            break;
                        case  APPROVE_T002://指定角色
                            resultInfo.setApproveRoleId(info.getRoleId());
                            String userIds=userRoleMapper.findUserIds(info.getRoleId());
                            if (StringUtils.isEmpty(userIds)){
                                ApproveTemplateInfo approveTemplateInfo = approveTemplateInfoMapper.selectApproveTemplateInfoById(Long.valueOf(regimeVo.getApproveTemplateId()));
                                EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(approveTemplateInfo.getCompanyId());
                                if (ecmpOrg!=null&&ecmpOrg.getParentId()!=0){
                                    //分公司
                                    userIds= userRoleMapper.findUsersByRoleKey(SUB_ADMIN_ROLE,ecmpOrg.getDeptId());
                                    if (StringUtils.isEmpty(userIds)){
                                        userIds= userRoleMapper.findUsersByRoleKey(ADMIN_ROLE,ecmpOrg.getDeptId());
                                    }
                                }else{
                                    //总公司
                                    userIds= userRoleMapper.findUsersByRoleKey(ADMIN_ROLE,ecmpOrg.getDeptId());
                                }
                            }
                            resultInfo.setApproveUserId(userIds);
                            break;
                        case  APPROVE_T003://指定多个员工
                            resultInfo.setApproveUserId(info.getUserId());
                            break;
                        case  APPROVE_T004://项目负责人
                            String projectLeader = getProjectLeader(applyInfo.getProjectId(),userId);
                            log.info("项目负责人审批:"+projectLeader);
                            resultInfo.setApproveUserId(projectLeader);
                            break;
                        }
                    applyApproveResultInfoMapper.insertApplyApproveResultInfo(resultInfo);
                }
            }
            log.info("申请单"+applyId+"初始化审批流成功");
        }
    }

    public String getProjectLeader(Long projectId,Long userId) throws Exception{
        //项目id不存在则查询当前申请人公司主管
        String leader=null;
        EcmpUser user = ecmpUserMapper.selectEcmpUserById(userId);
        if (projectId==null||projectId==Long.valueOf(ZERO)){
            leader=this.getOrgByDeptId(user.getDeptId());
        }else{
            leader=projectInfoMapper.findLeader(projectId);
            if (StringUtils.isEmpty(leader)){
                leader= this.getOrgByDeptId(user.getDeptId());
            }
        }
        if (StringUtils.isEmpty(leader)){
            throw new Exception("该公司未设置领导层");
        }
        return leader;
    }

    private String getOrgByDeptId(Long deptId){
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        if (DEPT_TYPE_ORG.equals(ecmpOrg.getDeptType())){//是公司
            return ecmpOrg.getLeader();
        }else{
            String ancestors = ecmpOrg.getAncestors();
            if (StringUtils.isNotEmpty(ancestors)){
                String[] split = ancestors.split(",");
                for (int i=split.length-2;i>=0;i--){
                    EcmpOrg org= ecmpOrgMapper.selectEcmpOrgById(Long.parseLong(split[i]));
                    if (DEPT_TYPE_ORG.equals(org.getDeptType())){
                        //是公司
                        return org.getLeader();
                    }
                }
            }
            return null;
        }
    }

    /**
     * 审批前校验当前审批人
     * @param journeyApplyDto
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<ApplyApproveResultInfo> beforeInspect(ApplyDTO journeyApplyDto,Long userId) throws Exception{
        ApplyInfo applyInfo1 = applyInfoMapper.selectApplyInfoById(journeyApplyDto.getApplyId());
        if (ObjectUtils.isEmpty(applyInfo1)){
            throw new Exception("行程申请单不存在!");
        }
        if (!ApplyStateConstant.ON_APPLYING.equals(applyInfo1.getState())){
            throw new Exception("该申请单已审批或已撤销");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(applyInfo1.getJourneyId());
        if (ObjectUtils.isEmpty(journeyInfo)){
            throw new Exception("行程申请单不存在!");
        }
        if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyInfo1.getApplyType())){
            if (journeyInfo.getUseCarTime().getTime()<System.currentTimeMillis()){
                //申请单已过期
                applyInfoService.updateApplyState(journeyApplyDto.getApplyId(),ApplyStateConstant.EXPIRED_APPLY,ApproveStateEnum.EXPIRED_APPROVE_STATE.getKey(),userId);
                throw new Exception("申请单:"+applyInfo1.getApplyId()+"已过期");
            }
        }else{//差旅
            int i = DateFormatUtils.compareDay(journeyInfo.getStartDate(), new Date());
            if (i==ONE){
                //申请单已过期
                applyInfoService.updateApplyState(journeyApplyDto.getApplyId(),ApplyStateConstant.EXPIRED_APPLY,ApproveStateEnum.EXPIRED_APPROVE_STATE.getKey(),userId);
                throw new Exception("申请单:"+applyInfo1.getApplyId()+"已过期");
            }
        }
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(applyInfo1.getRegimenId());
        List<ApplyApproveResultInfo> applyApproveResultInfos = this.selectByUserId(journeyApplyDto.getApplyId(), userId,null);
        if (CollectionUtils.isEmpty(applyApproveResultInfos)){
            throw new Exception("您未有此申请单的审批权限");
        }
        List<ApplyApproveResultInfo> resultInfoList = this.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(journeyApplyDto.getApplyId(),regimeInfo.getApproveTemplateId()));
        //所有待审批的记录
        List<ApplyApproveResultInfo> collect = resultInfoList.stream().filter(p -> ApproveStateEnum.WAIT_APPROVE_STATE.getKey().equals(p.getState()) && org.apache.commons.lang3.StringUtils.isBlank(p.getApproveResult())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)){
            for (ApplyApproveResultInfo info:collect){
                if (org.apache.commons.lang3.StringUtils.isBlank(info.getApproveUserId())||!info.getApproveUserId().contains(String.valueOf(userId))){
                    throw new Exception("此申请单还未到达您审批!");
                }
            }
        }else{
            String state=ApproveStateEnum.format(resultInfoList.get(0).getState());
            throw new Exception("该申请单:"+resultInfoList.get(0).getApplyId()+ state);
        }
        return resultInfoList;
    }

    /**
     * 修改下一审批节点状态为待审批
     * @param noArrivecollect
     * @param nextNodeId
     * @param applyId
     * @param userId
     */
    private void updateNextApproveResult(List<ApplyApproveResultInfo> noArrivecollect,Long nextNodeId,Long applyId,Long userId){
        //下一审批人修改为待审批
        if (CollectionUtils.isNotEmpty(noArrivecollect)) {
            for (ApplyApproveResultInfo resultInfo:noArrivecollect){
                if(ApproveStateEnum.NOT_ARRIVED_STATE.getKey().equals(resultInfo.getState())&&nextNodeId.equals(resultInfo.getApproveNodeId())){
                    resultInfo.setState(ApproveStateEnum.WAIT_APPROVE_STATE.getKey());
                    resultInfo.setUpdateTime(new Date());
                    resultInfo.setUpdateBy(userId+"");
                    this.updateApplyApproveResultInfo(resultInfo);
                    //给下一审批人发送消息
                    ecmpMessageService.sendNextApproveUsers(resultInfo.getApproveUserId(),applyId,userId);
                }
            }
        }
    }

    /**
     * 修改当前审批记录为审批通过
     * @param collect
     * @param userId
     * @param result
     * @param content
     */
    private void updateApproveResult(List<ApplyApproveResultInfo> collect, Long userId,String result,String content) {
        if (CollectionUtils.isNotEmpty(collect)) {
            //下一审批人为多个
            for (ApplyApproveResultInfo info : collect) {
                info.setApproveResult(result);
                info.setContent(content);
                info.setState(ApproveStateEnum.COMPLETE_APPROVE_STATE.getKey());
                info.setUpdateBy(String.valueOf(userId));
                info.setUpdateTime(new Date());
                applyApproveResultInfoMapper.updateApplyApproveResultInfo(info);
            }
        }
    }

}
