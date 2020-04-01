package com.hq.ecmp.mscore.service.impl;

import java.util.*;

import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.constant.ApproveTypeEnum;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IApplyApproveResultInfoService;
import com.hq.ecmp.mscore.vo.ApprovalInfoVO;
import com.hq.ecmp.mscore.vo.UserVO;
import com.hq.ecmp.util.SortListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hq.ecmp.constant.CommonConstant.DEPT_TYPE_ORG;
import static com.hq.ecmp.constant.CommonConstant.ZERO;


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
     * 初始化审批流
     * @param applyId 申请id
     * @param regimenId 用车制度id
     * @param userId 登录人id
     */
    @Override
    public void initApproveResultInfo(Long applyId,Long regimenId,Long userId) throws Exception {
        //查询审批模板
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimenId);
        if (regimeVo!=null){
            if (CommonConstant.NO_PASS.equals(regimeVo.getNeedApprovalProcess())){
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
                            UserVO deptUser=ecmpUserMapper.findDeptLeader(Long.parseLong(applyInfo.getCreateBy()));
                            if (deptUser==null){
                                EcmpUser user = ecmpUserMapper.selectEcmpUserById(Long.parseLong(applyInfo.getCreateBy()));
                                deptUser= this.getOrgByDeptId(user.getDeptId());
                            }
                            if (deptUser==null){
                                log.error("用车制度:"+regimenId+"对应审批模板项目主管审批未设置主管");
                                throw new Exception("该公司未设置领导层");
                            }
                            resultInfo.setApproveUserId(String.valueOf(deptUser.getUserId()));
                            break;
                        case  APPROVE_T002://指定角色
                            resultInfo.setApproveRoleId(info.getRoleId());
                            String userIds=userRoleMapper.findUserIds(info.getRoleId());
                            resultInfo.setApproveUserId(userIds);
                            break;
                        case  APPROVE_T003://指定多个员工
                            resultInfo.setApproveUserId(info.getUserId());
                            break;
                        case  APPROVE_T004://项目负责人
                            UserVO projectLeader = getProjectLeader(applyInfo.getProjectId(),userId);
                            log.info("项目负责人审批:"+projectLeader.toString());
                            resultInfo.setApproveUserId(String.valueOf(projectLeader.getUserId()));
                            break;
                        }
                    applyApproveResultInfoMapper.insertApplyApproveResultInfo(resultInfo);
                }
            }
        }
    }

    public UserVO getProjectLeader(Long projectId,Long userId) throws Exception{
        //项目id不存在则查询当前申请人公司主管
        UserVO vo=null;
        EcmpUser user = ecmpUserMapper.selectEcmpUserById(userId);
        if (projectId==null||projectId==Long.valueOf(CommonConstant.SWITCH_ON)){
            vo=this.getOrgByDeptId(user.getDeptId());
        }else{
            vo=projectInfoMapper.findLeader(projectId);
            if (vo==null){
                vo= this.getOrgByDeptId(user.getDeptId());
            }
        }
        if (vo==null){
            throw new Exception("该公司未设置领导层");
        }
        return vo;
    }

    private UserVO getOrgByDeptId(Long deptId){
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        if (DEPT_TYPE_ORG.equals(ecmpOrg.getDeptType())){//是公司
            return ecmpUserMapper.findDeptLeader(deptId);
        }else{
            String ancestors = ecmpOrg.getAncestors();
            if (StringUtils.isNotEmpty(ancestors)){
                String[] split = ancestors.split(",");
                for (int i=split.length-2;i>=0;i--){
                    EcmpOrg org= ecmpOrgMapper.selectEcmpOrgById(Long.parseLong(split[i]));
                    if (DEPT_TYPE_ORG.equals(org.getDeptType())){//是公司
                        return ecmpUserMapper.findDeptLeader(org.getDeptId());
                    }
                }
            }
            return null;
        }
    }

}
